package io.github.yb.server

import com.github.benmanes.caffeine.cache.Caffeine
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.SecurityContext
import io.github.yb.server.excepciones.ExcepcionAutentificacion
import io.github.yb.server.excepciones.ExcepcionInicializacion
import io.github.yb.server.google.FiltroTokenGoogle
import io.github.yb.server.google.ProveedorAutentificacionGoogle
import io.github.yb.server.repositorios.RepositorioUsuario
import io.github.yb.server.servicios.SystemDotenv
import io.github.yb.server.util.DetallesUsuarioAnonimo
import io.github.yb.server.util.Perfiles
import io.github.yb.server.util.detallesDeUsuario
import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class ConfiguracionRest(
    val dotenv: SystemDotenv,
    val repositorioUsuario: RepositorioUsuario
) {

    companion object {
        private val logger = LoggerFactory.getLogger(ConfiguracionRest::class.java)
    }

    val publicKey = dotenv.get("RSA_PUB")?.let {
        val decoded = Base64.getDecoder().decode(it)
        val spec = X509EncodedKeySpec(decoded)
        KeyFactory.getInstance("RSA").generatePublic(spec)
    } ?: throw ExcepcionInicializacion(ConfiguracionRest::class, "publicKey", "dotenv no encuentra la clave RSA_PUB")

    val privateKey = dotenv.get("RSA_PRIV")?.let {
        val decoded = Base64.getDecoder().decode(it)
        val spec = PKCS8EncodedKeySpec(decoded)
        KeyFactory.getInstance("RSA").generatePrivate(spec)
    } ?: throw ExcepcionInicializacion(ConfiguracionRest::class, "privateKey", "dotenv no encuentra la clave RSA_PRIV")

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        filtroTokenGoogle: FiltroTokenGoogle,
        authManager: AuthenticationManager
    ): SecurityFilterChain {
        return http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/v3/api-docs").permitAll()
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                    .anyRequest().authenticated()
            }
            .csrf { csrf -> csrf.ignoringRequestMatchers("/v1/token") }
            .httpBasic(Customizer.withDefaults())
            .authenticationManager(authManager)
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt(Customizer.withDefaults())
            }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(filtroTokenGoogle, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { exceptions ->
                exceptions
                    .authenticationEntryPoint(BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(BearerTokenAccessDeniedHandler())
            }
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun users(
        encriptador: PasswordEncoder,
        perfiles: Perfiles
    ): UserDetailsService {
        return UserDetailsService { username ->
            if (username.startsWith("anonimo")) {
                logger.info("autentificando usuario anónimo $username")
                DetallesUsuarioAnonimo(encriptador, username, perfiles)
            } else {
                logger.info("intentando autentificar usuario $username")
                repositorioUsuario.encontrarPorUsuario(username)
                    .also { usuario ->
                        if (usuario?.perfil == "usuarioGoogle") {
                            logger.warn(
                                "El usuario $username debería autentificarse con un token de google" +
                                        "pero está intentando usar autentificación simple."
                            )
                            throw ExcepcionAutentificacion(
                                "El usuario $username debería autentificarse con un token de google" +
                                        "pero está intentando usar autentificación simple.",
                                "bean personalizado UserDetailsService"
                            )
                        }
                    }
                    ?.detallesDeUsuario(perfiles)
                    .also { logger.info("usuario $username encontrado") }
                    ?: (throw UsernameNotFoundException("usuario $username no encontrado")
                        .also { logger.info("no se pudo encontrar al usuario $username") })
            }
        }
    }

    @Bean
    fun authenticationManager(
        proveedorAutentificacionGoogle: ProveedorAutentificacionGoogle,
        servicioDetallesUsuario: UserDetailsService,
        encriptador: PasswordEncoder,
        jwtDecoder: JwtDecoder
    ): AuthenticationManager {
        val daoProvider = DaoAuthenticationProvider(servicioDetallesUsuario).apply {
            setPasswordEncoder(encriptador)
        }
        val jwtProvider = JwtAuthenticationProvider(jwtDecoder)
        return ProviderManager(listOf(daoProvider, proveedorAutentificacionGoogle, jwtProvider))
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(publicKey as RSAPublicKey).build()
    }

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val jwk = RSAKey.Builder(publicKey as RSAPublicKey).privateKey(privateKey).build()
        val jwks = ImmutableJWKSet<SecurityContext>(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }

    @Bean
    fun timedAspect(registry: MeterRegistry): TimedAspect {
        return TimedAspect(registry);
    }

    @Bean
    fun caffeineConfig(): Caffeine<Any, Any> {
        return Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).maximumSize(5000)
    }

    @Bean
    fun cacheManager(caffeine: Caffeine<Any, Any>): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager()
        caffeineCacheManager.setCaffeine(caffeine)
        return caffeineCacheManager
    }
}
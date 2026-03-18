package io.github.yb.server

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.SecurityContext
import io.github.yb.server.excepciones.ExcepcionInicializacion
import io.github.yb.server.repositorios.RepositorioUsuario
import io.github.yb.server.servicios.SystemDotenv
import io.github.yb.server.util.DetallesUsuarioAnonimo
import io.github.yb.server.util.detallesDeUsuario
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler
import org.springframework.security.web.SecurityFilterChain
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Configuration
class ConfiguracionRest(
    val dotenv: SystemDotenv,
    val repositorioUsuario: RepositorioUsuario
) {

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
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests { authorize -> authorize.anyRequest().authenticated() }
            .csrf { csrf -> csrf.ignoringRequestMatchers("/token") }
            .httpBasic(Customizer.withDefaults())
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt(Customizer.withDefaults()).jwt(Customizer.withDefaults())
            }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { exceptions ->
                exceptions
                    .authenticationEntryPoint(BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(BearerTokenAccessDeniedHandler())
            }.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun users(encriptador: PasswordEncoder): UserDetailsService {
        return UserDetailsService { username ->
            if(username == "anonimo") {
                DetallesUsuarioAnonimo(encriptador)
            } else {
                repositorioUsuario.encontrarPorUsuario(username)?.detallesDeUsuario()
                    ?: throw UsernameNotFoundException("usuario $username no encontrado")
            }
        }
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
}
package io.github.yb.server.web

import io.github.yb.server.excepciones.ExcepcionAutentificacion
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant


@RestController
class TokenController(
    val encoder: JwtEncoder
) {

    companion object {
        private val logger = LoggerFactory.getLogger(TokenController::class.java)
    }

    @Timed("token")
    @PostMapping("/v1/token")
    fun token(autentificacion: Authentication): ResponseEntity<String> {
        if (!autentificacion.isAuthenticated) {
            logger.warn("${(autentificacion.principal as? UserDetails)?.username ?: autentificacion.name} intentó obtener un token sin autentificarse")
            throw ExcepcionAutentificacion("solicitud sin autentificación exitosa", "endpoint /token")
        }
        val now = Instant.now()
        val expiry = 43200L
        val scope = autentificacion.authorities.joinToString(separator = " ") { it.authority!! }
        val claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiry))
            .subject(autentificacion.name)
            .claim("scope", scope)
            .build()
        logger.info("devolviendo token para ${(autentificacion.principal as? UserDetails)?.username ?: autentificacion.name}")
        return ResponseEntity.ok(encoder.encode(JwtEncoderParameters.from(claims)).tokenValue)
    }
}
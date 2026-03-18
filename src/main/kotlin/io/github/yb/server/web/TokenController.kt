package io.github.yb.server.web

import io.github.yb.server.excepciones.ExcepcionAutentificacion
import io.github.yb.server.repositorios.RepositorioUsuario
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
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

    @PostMapping("/token")
    fun token(autentificacion: Authentication): ResponseEntity<String> {
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
        return if(autentificacion.isAuthenticated){
            ResponseEntity.ok(encoder.encode(JwtEncoderParameters.from(claims)).tokenValue)
        } else {
            throw ExcepcionAutentificacion("solicitud sin autentificación exitosa", "endpoint /token")
        }
    }
}
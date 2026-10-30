package io.github.yb.server.google

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import io.github.yb.server.entidades.Usuario
import io.github.yb.server.excepciones.ExcepcionAutentificacion
import io.github.yb.server.repositorios.RepositorioUsuario
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class GoogleAuthenticationProvider(
    val googleAuthService: GoogleAuthService,
    val userDetailsService: UserDetailsService,
    val repositorioUsuario: RepositorioUsuario
): AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val googleIdToken = authentication.credentials as? String ?: throw ExcepcionAutentificacion(
            "la propiedad credentials del objeto authentication no es un string",
            "GoogleAuthenticationProvider"
        )
        val payload = googleAuthService.verificar(googleIdToken)
        if(repositorioUsuario.encontrarPorEmail(payload.email) == null){
            val nuevoUsuario = payload.crearUsuario()
            repositorioUsuario.crear(nuevoUsuario)
        }
        val userDetails = userDetailsService.loadUserByUsername(payload.email)
        return GoogleAuthenticationToken(googleIdToken, userDetails, userDetails.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return GoogleAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    private fun GoogleIdToken.Payload.crearUsuario(): Usuario{
        return Usuario(
            username = email.usuario(),
            pass = "contraseña que no va a usarse".encodeToByteArray(),
            email = email,
            universidad = "a determinar",
            contacto = "",
            perfil = "usuarioGoogle",
            OffsetDateTime.now()
        )
    }

    private fun String.usuario(): String {
        return substringBefore("@")
    }
}
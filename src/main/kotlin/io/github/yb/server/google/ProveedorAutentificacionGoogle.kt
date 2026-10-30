package io.github.yb.server.google

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.github.yb.server.entidades.Usuario
import io.github.yb.server.excepciones.ExcepcionAutentificacion
import io.github.yb.server.repositorios.RepositorioUsuario
import io.github.yb.server.servicios.SystemDotenv
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class ProveedorAutentificacionGoogle(
    val userDetailsService: UserDetailsService,
    val repositorioUsuario: RepositorioUsuario,
    val systemDotenv: SystemDotenv,
): AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val googleIdToken = authentication.credentials as? String ?: throw ExcepcionAutentificacion(
            "la propiedad credentials del objeto authentication no es un string",
            "GoogleAuthenticationProvider"
        )
        val payload = verificar(googleIdToken)
        if(repositorioUsuario.encontrarPorEmail(payload.email) == null){
            val nuevoUsuario = payload.crearUsuario()
            repositorioUsuario.crear(nuevoUsuario)
        }
        val userDetails = userDetailsService.loadUserByUsername(payload.email)
        return AutentificacionTokenGoogle(googleIdToken, userDetails, userDetails.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return AutentificacionTokenGoogle::class.java.isAssignableFrom(authentication)
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

    fun verificar(idTokenString: String): GoogleIdToken.Payload {
        val verificador = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(systemDotenv.get("WEB_CLIENT_ID")))
            .build() ?: throw ExcepcionAutentificacion(
            "no se puede crear el verificador de google",
            "GoogleAuthService"
        )
        val idToken = verificador.verify(idTokenString)

        return idToken?.payload ?: throw ExcepcionAutentificacion(
            "token de google no es válido",
            "GoogleAuthService"
        )
    }
}
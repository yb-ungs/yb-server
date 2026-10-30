package io.github.yb.server.servicios

import io.github.yb.server.excepciones.ExcepcionAutentificacion
import io.github.yb.server.modelos.DatosUsuario
import io.github.yb.server.repositorios.RepositorioUsuario
import io.github.yb.server.util.DetallesUsuarioAnonimo
import io.github.yb.server.util.Perfiles
import io.github.yb.server.util.datosUsuarioAnonimo
import io.github.yb.server.util.detallesDeUsuario
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class ServicioContextoSeguridad(
    val repositorioUsuario: RepositorioUsuario,
    val encriptador: PasswordEncoder,
    val perfiles: Perfiles
) {
    val usuario: String
        get() {
            val principal = SecurityContextHolder.getContext().authentication?.principal
                ?: throw ExcepcionAutentificacion(
                    "no se encuentra autentificación en el contexto",
                    "ServicioContextoSeguridad::usuario"
                )
            return when (principal) {
                is UserDetails -> principal.username
                is Jwt -> principal.subject
                else -> principal.toString()
            }
        }

    val detallesUsuario: UserDetails
        get() {
            val username = this.usuario
            if (username.startsWith("anonimo")) {
                return DetallesUsuarioAnonimo(encriptador, username, perfiles)
            }
            return repositorioUsuario.encontrarPorUsuario(username)?.detallesDeUsuario(perfiles)
                ?: throw ExcepcionAutentificacion(
                    "no se encuentra al usuario $username en la base de datos",
                    "ServicioContextoSeguridad::detallesUsuario"
                )
        }

    val datosUsuario: DatosUsuario
        get() {
            val username = this.usuario
            if (username.startsWith("anonimo")){
                val universidad = username.split("_")[1]
                return datosUsuarioAnonimo(
                    usuario = username,
                    universidad = universidad,
                    seUnioEl = OffsetDateTime.now().toString()
                )
            }
            val usuario = repositorioUsuario.encontrarPorUsuario(username)
                ?: throw ExcepcionAutentificacion(
                    "no se encuentra al usuario $username en la base de datos",
                    "ServicioContextoSeguridad::datosUsuario"
                )
            return DatosUsuario(usuario)
        }

    fun tieneAutoridad(autoridad: String): Boolean{
        return detallesUsuario.authorities.contains(GrantedAuthority { autoridad })
    }
}
package io.github.yb.server.servicios

import io.github.yb.server.excepciones.ExcepcionAutentificacion
import io.github.yb.server.modelos.DatosUsuario
import io.github.yb.server.repositorios.RepositorioUsuario
import io.github.yb.server.util.detallesDeUsuario
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.security.oauth2.jwt.Jwt

@Service
class ServicioContextoSeguridad(val repositorioUsuario: RepositorioUsuario) {
    val username: String
        get() {
            val principal = SecurityContextHolder.getContext().authentication?.principal
                ?: throw ExcepcionAutentificacion(
                    "no se encuentra autentificación en el contexto",
                    "SecurityContextService::getAuthenticatedUsername"
                )
            return when (principal) {
                is UserDetails -> principal.username
                is Jwt -> principal.subject
                else -> principal.toString()
            }
        }

    val userDetails: UserDetails
        get() {
            return repositorioUsuario.encontrarPorUsuario(username)?.detallesDeUsuario()
                ?: throw ExcepcionAutentificacion(
                    "no se encuentra al usuario $username en la base de datos",
                    "SecurityContextService::getAuthenticatedUser"
                )
        }

    val datosUsuario: DatosUsuario
        get() {
            val usuario = repositorioUsuario.encontrarPorUsuario(username)
                ?: throw ExcepcionAutentificacion(
                    "no se encuentra al usuario $username en la base de datos",
                    "SecurityContextService::getAuthenticatedUser"
                )
            return DatosUsuario(usuario)
        }
}
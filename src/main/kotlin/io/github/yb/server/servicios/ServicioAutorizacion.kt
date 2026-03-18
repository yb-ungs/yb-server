package io.github.yb.server.servicios

import io.github.yb.server.modelos.DatosUsuario
import io.github.yb.server.repositorios.RepositorioUsuario
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class ServicioAutorizacion(
    val servicioContextoSeguridad: ServicioContextoSeguridad,
    val repositorioUsuario: RepositorioUsuario
) {


    private fun UserDetails.puedeEditarTodo(): Boolean {
        return authorities.any { it.authority == "CREAR_MODIFICAR_BORRAR_TODO" }
    }

    private fun DatosUsuario.puedeVerEditarBorrarMismaUniversidad(username: String): Boolean {
        val usuario = repositorioUsuario.encontrarPorUsuario(username)
            ?: throw UsernameNotFoundException("usuario $username no encontrado")
        val detallesDeUsuario = servicioContextoSeguridad.userDetails
        return usuario.universidad == this.universidad && detallesDeUsuario.authorities
            .any { it.authority == "CREAR_MODIFICAR_BORRAR_UNIVERSIDAD" }
    }

    fun puedeAgregarUsuario(): Boolean {
        return servicioContextoSeguridad.userDetails.authorities.any { it.authority == "AGREGAR_USUARIO" }
    }

    fun puedeBorrarUsuario(): Boolean {
        return servicioContextoSeguridad.userDetails.authorities.any { it.authority == "BORRAR_USUARIO" }
    }

    fun puedeActualizarDatosPropios(username: String): Boolean {
        val detallesDeUsuario = servicioContextoSeguridad.userDetails
        return detallesDeUsuario.username == username
                && detallesDeUsuario.authorities.any { it.authority == "ACTUALIZAR_DATOS_PROPIOS" }
    }

    fun puedeCambiarPassPropia(username: String): Boolean {
        val detallesDeUsuario = servicioContextoSeguridad.userDetails
        return detallesDeUsuario.username == username || detallesDeUsuario.authorities.any { it.authority == "CAMBIAR_PASS_PROPIA" }
    }

    fun puedeCambiarPerfil(): Boolean {
        return servicioContextoSeguridad.userDetails.authorities.any { it.authority == "CAMBIAR_PERFIL" }
    }

    fun puedeVerDatosPropios(): Boolean {
        return servicioContextoSeguridad.userDetails.authorities.any { it.authority == "VER_DATOS_PROPIOS" }
    }

    fun puedeVerDatosUsuario(): Boolean {
        return servicioContextoSeguridad.userDetails.authorities.any { it.authority == "VER_DATOS_USUARIO" }
    }

    fun puedeVerUsuariosPorUniversidad(universidad: String): Boolean {
        return servicioContextoSeguridad.userDetails.authorities.any { it.authority == "VER_USUARIOS_UNIVERSIDAD" }
                && servicioContextoSeguridad.datosUsuario.universidad == universidad
    }
}
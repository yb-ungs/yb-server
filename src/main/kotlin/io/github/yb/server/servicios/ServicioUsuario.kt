package io.github.yb.server.servicios

import io.github.yb.server.entidades.Usuario
import io.github.yb.server.modelos.CambioPass
import io.github.yb.server.modelos.CambioPerfil
import io.github.yb.server.modelos.DatosUsuario
import io.github.yb.server.modelos.NuevoUsuario
import io.github.yb.server.repositorios.RepositorioUsuario
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class ServicioUsuario(
    val repositorioUsuario: RepositorioUsuario,
    val encriptador: PasswordEncoder,
    val servicioContextoSeguridad: ServicioContextoSeguridad
) {

    private fun NuevoUsuario.usuario(encriptador: PasswordEncoder): Usuario {
        return Usuario(
            username = username,
            pass = encriptador.encode(pass)!!.encodeToByteArray(),
            email = email,
            universidad = universidad,
            contacto = contacto ?: "",
            creadoEl = OffsetDateTime.now(),
            perfil = "usuario"
        )
    }
    fun obtenerUsuario(usuario: String): Usuario {
        return repositorioUsuario.encontrarPorUsuario(usuario)
            ?: throw UsernameNotFoundException("usuario $usuario no encontrado")
    }

    fun actualizar(datos: DatosUsuario) {
        val usuario = obtenerUsuario(datos.username)
        val registroActualizado = Usuario(
            username = usuario.username,
            pass = usuario.pass,
            email = datos.email,
            universidad = datos.universidad,
            contacto = datos.contacto,
            creadoEl = usuario.creadoEl,
            perfil = usuario.perfil
        )
        repositorioUsuario.actualizar(registroActualizado)
    }

    fun cambiarPass(cambioPass: CambioPass) {
        val usuario = obtenerUsuario(cambioPass.username)
        val registroActualizado = Usuario(
            username = usuario.username,
            pass = encriptador.encode(cambioPass.pass)!!.encodeToByteArray(),
            email = usuario.email,
            universidad = usuario.universidad,
            contacto = usuario.contacto,
            creadoEl = usuario.creadoEl,
            perfil = usuario.perfil
        )
        repositorioUsuario.actualizar(registroActualizado)
    }

    fun cambiarPeril(cambioPerfil: CambioPerfil) {
        val usuario = obtenerUsuario(cambioPerfil.username)
        val registroActualizado = Usuario(
            username = usuario.username,
            pass = usuario.pass,
            email = usuario.email,
            universidad = usuario.universidad,
            contacto = usuario.contacto,
            creadoEl = usuario.creadoEl,
            perfil = cambioPerfil.perfil
        )
        repositorioUsuario.actualizar(registroActualizado)
    }

    fun crear(nuevoUsuario: NuevoUsuario) {
        repositorioUsuario.crear(nuevoUsuario.usuario(encriptador))
    }

    fun borrar(username: String) {
        repositorioUsuario.delete(
            repositorioUsuario.encontrarPorUsuario(username)
                ?: throw UsernameNotFoundException("usuario $username no encontrado")
        )
    }

    fun encontrarPorNombre(username: String): DatosUsuario {
        return DatosUsuario(
            repositorioUsuario.encontrarPorUsuario(username)
                ?: throw UsernameNotFoundException("usuario $username no encontrado")
        )
    }

    fun datosPropios(): DatosUsuario {
        return servicioContextoSeguridad.datosUsuario
    }

    fun encontrarPorUniversidad(universidad: String): List<DatosUsuario> {
        return repositorioUsuario.encontrarPorUniversidad(universidad).map { DatosUsuario(it) }
    }

    fun usuarioExiste(usuario: String): Boolean {
        return repositorioUsuario.encontrarPorUsuario(usuario) != null
    }
}
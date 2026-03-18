package io.github.yb.server.modelos

import io.github.yb.server.entidades.Usuario
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.OffsetDateTime

data class NuevoUsuario (
    val username: String,
    val pass: String,
    val email: String,
    val universidad: String,
    val contacto: String?,
    val creadoEl: String
) {
    fun usuario(encriptador: PasswordEncoder): Usuario {
        return Usuario(
            username = username,
            pass = encriptador.encode(pass)!!.encodeToByteArray(),
            email = email,
            universidad = universidad,
            contacto = contacto ?: "",
            creadoEl = OffsetDateTime.parse(creadoEl),
            perfil = "usuario"
        )
    }
}
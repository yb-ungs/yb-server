package io.github.yb.server.modelos

import io.github.yb.server.entidades.Usuario
import io.swagger.v3.oas.annotations.media.Schema
import java.time.OffsetDateTime

data class DatosUsuario (
    val username: String,
    val email: String,
    val universidad: String,
    val contacto: String,
    val perfil: String,
    @Schema(description = "string en formato ISO")
    val seUnioEl: String
) {
    constructor(usuario: Usuario) : this(
        username = usuario.username,
        email = usuario.email,
        universidad = usuario.universidad,
        contacto = usuario.contacto ?: "",
        perfil = usuario.perfil,
        seUnioEl = usuario.creadoEl.toString()
    )
}
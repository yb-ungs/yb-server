package io.github.yb.server.modelos

import io.github.yb.server.entidades.Usuario
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.OffsetDateTime

data class NuevoUsuario (
    val username: String,
    val pass: String,
    val email: String,
    val universidad: String,
    val contacto: String?,
)
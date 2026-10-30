package io.github.yb.server.modelos

import io.swagger.v3.oas.annotations.media.Schema
import java.time.OffsetDateTime

data class DatosCarreraUsuario(
    val id: String,
    val carrera: String,
    val username: String,
    @Schema(description = "string en formato ISO")
    val creadoEl: String
)

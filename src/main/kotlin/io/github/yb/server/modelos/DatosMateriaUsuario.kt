package io.github.yb.server.modelos

import io.swagger.v3.oas.annotations.media.Schema
import java.time.OffsetDateTime

data class DatosMateriaUsuario(
    val id: String,
    val idMateria: String,
    val idUsuario: String,
    @Schema(description = "string en formato ISO")
    val creadoEl: String
)
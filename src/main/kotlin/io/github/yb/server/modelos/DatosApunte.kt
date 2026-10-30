package io.github.yb.server.modelos

import io.swagger.v3.oas.annotations.media.Schema
import java.time.OffsetDateTime

data class DatosApunte(
    val id: String,
    val idApunte: String,
    val idMateria: String,
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    val universidad: String,
    @Schema(description = "string en formato ISO")
    val creadoEl: String
)
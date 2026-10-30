package io.github.yb.server.modelos

import io.swagger.v3.oas.annotations.media.Schema
import java.time.OffsetDateTime

data class DatosAulaHorario(
    val id: String,
    val idMateria: String,
    val idAula: String,
    val comision: String,
    val horario: String,
    val universidad: String,
    @Schema(description = "string en formato ISO")
    val creadoEl: String
)
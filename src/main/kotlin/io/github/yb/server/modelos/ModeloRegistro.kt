package io.github.yb.server.modelos

import io.swagger.v3.oas.annotations.media.Schema
import java.time.OffsetDateTime

data class ModeloRegistro (
    val id: String,
    @Schema(description = "string en formato ISO")
    val creadoEl: String,
    val nivel: String,
    val nombre: String,
    val hilo: String,
    val mensaje: String,
    val traza: String
)
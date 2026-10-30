package io.github.yb.server.modelos

import io.swagger.v3.oas.annotations.media.Schema

data class DatosAnuncio(
    val id: String,
    val autor: String,
    val titulo: String,
    val contenido: String,
    val universidad: String,
    val agrupacion: String,
    @Schema(description = "string en formato ISO")
    val creadoEl: String,
)

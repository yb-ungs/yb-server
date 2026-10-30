package io.github.yb.server.modelos

import java.time.OffsetDateTime

data class ModeloAnuncio(
    val id: String,
    val autor: String,
    val titulo: String,
    val contenido: String,
    val universidad: String,
    val agrupacion: String,
    val creadoEl: String,
)

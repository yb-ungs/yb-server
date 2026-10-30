package io.github.yb.server.modelos

import java.time.OffsetDateTime

data class ModeloApunte(
    val id: String,
    val idApunte: String,
    val idMateria: String,
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    val universidad: String,
    val creadoEl: OffsetDateTime
)
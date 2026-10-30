package io.github.yb.server.entidades

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "anuncios")
data class Anuncio (
    @Id
    val id: String,
    val autor: String,
    val titulo: String,
    val contenido: String,
    val universidad: String,
    val agrupacion: String,
    val creadoEl: OffsetDateTime,
)
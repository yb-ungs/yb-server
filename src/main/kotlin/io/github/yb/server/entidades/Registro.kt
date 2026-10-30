package io.github.yb.server.entidades

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "registros")
data class Registro (
    @Id
    val id: String,
    val creadoEl: OffsetDateTime,
    val nivel: String,
    val nombre: String,
    val hilo: String,
    val mensaje: String,
    val traza: String
)
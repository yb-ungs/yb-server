package io.github.yb.server.entidades

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "apuntes")
data class Apuntes (
    @Id
    val id: String,
    val idApunte: String,
    val idMateria: String,
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    val universidad: String,
    val creadoEl: OffsetDateTime,
)
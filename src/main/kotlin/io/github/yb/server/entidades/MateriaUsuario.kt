package io.github.yb.server.entidades

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "materiasusuario")
data class MateriaUsuario(
    @Id
    val id: String,
    val idMateria: String,
    val idUsuario: String,
    val creadoEl: OffsetDateTime
)
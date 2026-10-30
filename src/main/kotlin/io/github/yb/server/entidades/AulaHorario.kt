package io.github.yb.server.entidades

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "aulashorarios")
data class AulaHorario(
    @Id
    val id: String,
    val idMateria: String,
    val idAula: String,
    val comision: String,
    val horario: String,
    val universidad: String,
    val creadoEl: OffsetDateTime
)
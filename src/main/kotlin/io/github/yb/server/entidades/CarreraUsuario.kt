package io.github.yb.server.entidades

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "carrerasusuario")
data class CarreraUsuario(
    @Id
    val id: String,
    val carrera: String,
    val username: String,
    val creadoEl: OffsetDateTime
)
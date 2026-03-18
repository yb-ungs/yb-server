package io.github.yb.server.entidades

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "usuarios")
class Usuario (
    @Id
    val username: String,
    val pass: ByteArray,
    val email: String,
    val universidad: String,
    val contacto: String,
    val perfil: String,
    val creadoEl: OffsetDateTime
)
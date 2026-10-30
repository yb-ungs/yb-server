package io.github.yb.server.entidades

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "materias")
data class Materia (
    @Id
    val id: String,
    val idMateria: String,
    val nombre: String,
    val universidad: String
)



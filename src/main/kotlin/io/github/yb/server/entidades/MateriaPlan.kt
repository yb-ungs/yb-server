package io.github.yb.server.entidades

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "materiasplan")
data class MateriaPlan(
    @Id
    val id: String,
    val idMateria: String,
    val correlativas: String,
    val idPlan: String,
    val cuatrimestre: String,
    val carrera: String,
    val plan: String,
    val universidad: String
)
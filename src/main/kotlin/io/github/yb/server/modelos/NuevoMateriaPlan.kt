package io.github.yb.server.modelos

data class NuevoMateriaPlan(
    val idMateria: String,
    val correlativas: String,
    val idPlan: String,
    val cuatrimestre: String,
    val carrera: String,
    val plan: String,
    val universidad: String
)
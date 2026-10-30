package io.github.yb.server.repositorios

data class DatosMateriaPlan(
    val id: String,
    val idMateria: String,
    val correlativas: String,
    val idPlan: String,
    val cuatrimestre: String,
    val carrera: String,
    val plan: String,
    val universidad: String
)
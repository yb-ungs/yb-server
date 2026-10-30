package io.github.yb.server.modelos

import io.swagger.v3.oas.annotations.media.Schema

data class DatosMateriaPlan(
    val id: String,
    val idMateria: String,
    @Schema(description = "ids de materias correlativas separados por espacios")
    val correlativas: String,
    val idPlan: String,
    val cuatrimestre: String,
    val carrera: String,
    val plan: String,
    val universidad: String
)
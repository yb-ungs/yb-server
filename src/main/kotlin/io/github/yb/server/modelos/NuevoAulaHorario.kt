package io.github.yb.server.modelos

data class NuevoAulaHorario (
    val idMateria: String,
    val idAula: String,
    val comision: String,
    val horario: String,
    val universidad: String,
)
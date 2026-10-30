package io.github.yb.server.modelos

import io.swagger.v3.oas.annotations.media.Schema

data class NuevoMateriaUsuario(
    val idMateria: String,
    val idUsuario: String,
)
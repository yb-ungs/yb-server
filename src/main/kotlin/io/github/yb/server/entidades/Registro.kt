package io.github.yb.server.entidades

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id

@Entity
@Table(name = "usuarios")
data class Regitro (
    val id: String,
    
)
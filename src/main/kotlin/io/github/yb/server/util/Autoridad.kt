package io.github.yb.server.util

import io.github.yb.server.servicios.ServicioHash
import org.springframework.security.core.GrantedAuthority

class Autoridad(
    private val autoridad: String,
    private val servicioHash: ServicioHash
) : GrantedAuthority {

    override fun getAuthority(): String {
        return autoridad
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Autoridad) return false
        return other.autoridad == autoridad
    }

    override fun hashCode(): Int {
        return servicioHash.hash(autoridad)
    }

    override fun toString(): String {
        return autoridad
    }
}
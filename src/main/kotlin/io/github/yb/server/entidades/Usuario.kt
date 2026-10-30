package io.github.yb.server.entidades

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "usuarios")
data class Usuario (
    @Id
    val username: String,
    val pass: ByteArray,
    val email: String,
    val universidad: String,
    val contacto: String,
    val perfil: String,
    val creadoEl: OffsetDateTime
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Usuario

        if (username != other.username) return false
        if (!pass.contentEquals(other.pass)) return false
        if (email != other.email) return false
        if (universidad != other.universidad) return false
        if (contacto != other.contacto) return false
        if (perfil != other.perfil) return false
        if (creadoEl != other.creadoEl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + pass.contentHashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + universidad.hashCode()
        result = 31 * result + contacto.hashCode()
        result = 31 * result + perfil.hashCode()
        result = 31 * result + creadoEl.hashCode()
        return result
    }
}
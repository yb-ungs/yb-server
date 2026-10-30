package io.github.yb.server.util

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder

class DetallesUsuarioAnonimo(
    encriptador: PasswordEncoder,
    val usuario: String,
    perfiles: Perfiles
) : UserDetails {
    val pass = encriptador.encode("anonimo")
    val autoridades = perfiles.anonimo

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return autoridades
    }

    override fun getPassword(): String? {
        return pass
    }

    override fun getUsername(): String {
        return usuario
    }
}
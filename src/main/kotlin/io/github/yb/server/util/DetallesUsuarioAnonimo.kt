package io.github.yb.server.util

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component


class DetallesUsuarioAnonimo(encriptador: PasswordEncoder) : UserDetails {

    val usuario = "anonimo"
    val pass = encriptador.encode("anonimo")
    val autoridades = autoridadesDePerfil("anonimo")

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
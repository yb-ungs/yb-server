package io.github.yb.server.util

import io.github.yb.server.entidades.Usuario
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class DetallesDeUsuario(val usuario: Usuario) : UserDetails{
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return autoridadesDePerfil(usuario.perfil)
    }

    override fun getPassword(): String {
        return usuario.pass.decodeToString()
    }

    override fun getUsername(): String {
        return usuario.username
    }

}
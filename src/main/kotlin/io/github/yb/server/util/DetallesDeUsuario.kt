package io.github.yb.server.util

import io.github.yb.server.entidades.Usuario
import io.github.yb.server.excepciones.ExcepcionAutorizacion
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class DetallesDeUsuario(
    val usuario: Usuario,
    val perfiles: Perfiles
) : UserDetails{

    private val autoridades by lazy {
        when (usuario.perfil) {
            "anonimo" -> perfiles.anonimo

            "usuario" -> perfiles.usuario

            "usuarioGoogle" -> perfiles.usuarioGoogle

            "adminUniversidad" -> perfiles.adminUniversidad

            "admin" -> perfiles.admin

            else -> throw ExcepcionAutorizacion(
                "perfil ${usuario.perfil} no encontrado", "DetallesDeUsuario"
            )
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return autoridades
    }

    override fun getPassword(): String {
        return usuario.pass.decodeToString()
    }

    override fun getUsername(): String {
        return usuario.username
    }

}
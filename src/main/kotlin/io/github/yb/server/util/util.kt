package io.github.yb.server.util

import io.github.yb.server.entidades.Usuario
import io.github.yb.server.modelos.DatosUsuario
import io.github.yb.server.servicios.ServicioHash
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

fun Permiso.autoridad(servicioHash: ServicioHash): GrantedAuthority {
    return Autoridad(name, servicioHash)
}

fun Usuario.detallesDeUsuario(perfiles: Perfiles): UserDetails {
    return DetallesDeUsuario(this, perfiles)
}

fun datosUsuarioAnonimo(
    usuario: String,
    universidad: String,
    seUnioEl: String
): DatosUsuario {

    return DatosUsuario(
        username = usuario,
        universidad = universidad,
        perfil = "anonimo",
        seUnioEl = seUnioEl,
        email = "",
        contacto = ""
    )
}

internal fun List<Permiso>.autoridades(servicioHash: ServicioHash): SetAutoridades {
    return SetAutoridades(map { it.autoridad(servicioHash) }, servicioHash)
}
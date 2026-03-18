package io.github.yb.server.util

import io.github.yb.server.entidades.Usuario
import io.github.yb.server.excepciones.ExcepcionAutorizacion
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

fun autoridadesDePerfil(perfil: String): List<GrantedAuthority> {
    return when(perfil) {
        "anonimo" -> listOf(
            Permiso.LEER_TABLA_PUBLICA,
            Permiso.AGREGAR_USUARIO
        )
        "usuario" -> listOf(
            Permiso.LEER_TABLA_PUBLICA,
            Permiso.LEER_TABLA_PRIVADA,
            Permiso.CREAR_MODIFICAR_BORRAR_PROPIOS,
            Permiso.CAMBIAR_PASS_PROPIA,
            Permiso.AGREGAR_USUARIO,
            Permiso.ACTUALIZAR_DATOS_PROPIOS,
            Permiso.VER_DATOS_PROPIOS,
        )
        "militante" -> listOf(
            Permiso.LEER_TABLA_PUBLICA,
            Permiso.LEER_TABLA_PRIVADA,
            Permiso.CREAR_MODIFICAR_BORRAR_UNIVERSIDAD,
            Permiso.CAMBIAR_PASS_PROPIA,
            Permiso.AGREGAR_USUARIO,
            Permiso.ACTUALIZAR_DATOS_PROPIOS,
            Permiso.VER_DATOS_PROPIOS,
            Permiso.VER_DATOS_USUARIO,
            Permiso.VER_USUARIOS_UNIVERSIDAD
        )
        "admin" -> listOf(
            Permiso.LEER_TABLA_PUBLICA,
            Permiso.LEER_TABLA_PRIVADA,
            Permiso.CREAR_MODIFICAR_BORRAR_TODO,
            Permiso.CAMBIAR_PASS_PROPIA,
            Permiso.AGREGAR_USUARIO,
            Permiso.BORRAR_USUARIO,
            Permiso.ACTUALIZAR_DATOS_PROPIOS,
            Permiso.CAMBIAR_PERFIL,
            Permiso.VER_DATOS_PROPIOS,
            Permiso.VER_DATOS_USUARIO,
            Permiso.VER_USUARIOS_UNIVERSIDAD
        )
        else -> throw ExcepcionAutorizacion("perfil $perfil no encontrado", "usuarioAUserDetails")
    }.map { it.authority() }
}

fun Permiso.authority(): GrantedAuthority{
    return GrantedAuthority { name }
}


fun Usuario.detallesDeUsuario(): UserDetails {
    return DetallesDeUsuario(this)
}
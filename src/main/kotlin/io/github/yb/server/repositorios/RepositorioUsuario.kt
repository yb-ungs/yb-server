package io.github.yb.server.repositorios

import io.github.yb.server.entidades.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Repository
interface RepositorioUsuario : JpaRepository<Usuario, String> {
    @Query(value = "SELECT * FROM usuarios WHERE username = :usuario", nativeQuery = true)
    fun encontrarPorUsuario(@Param("usuario") usuario: String): Usuario?

    @Query(value = "SELECT * FROM usuarios WHERE universidad = :universidad", nativeQuery = true)
    fun encontrarPorUniversidad(@Param("universidad") universidad: String): List<Usuario>

    @Query(value = "SELECT * FROM usuarios WHERE email = :email", nativeQuery = true)
    fun encontrarPorEmail(@Param("email") email: String): Usuario?

    @Modifying
    @Transactional
    @Query(
        value = """
        INSERT INTO usuarios (username, pass, email, universidad, contacto, perfil, creado_el)
        VALUES (:username, :pass, :email, :universidad, :contacto, :perfil, :creadoEl)
    """, nativeQuery = true
    )
    fun crear(
        @Param("username") username: String,
        @Param("pass") pass: ByteArray,
        @Param("email") email: String,
        @Param("universidad") universidad: String,
        @Param("contacto") contacto: String,
        @Param("perfil") perfil: String,
        @Param("creadoEl") creadoEl: OffsetDateTime
    )

    fun crear(usuario: Usuario) {
        crear(
            username = usuario.username,
            pass = usuario.pass,
            email = usuario.email,
            universidad = usuario.universidad,
            contacto = usuario.contacto,
            perfil = usuario.perfil,
            creadoEl = usuario.creadoEl
        )
    }

    @Modifying
    @Transactional
    @Query(
        value = """
        UPDATE usuarios
        SET pass = :pass,
            email = :email,
            universidad = :universidad,
            contacto = :contacto,
            perfil = :perfil,
            creado_el = :creadoEl
        WHERE username = :username
    """, nativeQuery = true
    )
    fun actualizar(
        @Param("username") username: String,
        @Param("pass") pass: ByteArray,
        @Param("email") email: String,
        @Param("universidad") universidad: String,
        @Param("contacto") contacto: String,
        @Param("perfil") perfil: String,
        @Param("creadoEl") creadoEl: OffsetDateTime
    )

    fun actualizar(usuario: Usuario) {
        actualizar(
            username = usuario.username,
            pass = usuario.pass,
            email = usuario.email,
            universidad = usuario.universidad,
            contacto = usuario.contacto,
            perfil = usuario.perfil,
            creadoEl = usuario.creadoEl
        )
    }

}
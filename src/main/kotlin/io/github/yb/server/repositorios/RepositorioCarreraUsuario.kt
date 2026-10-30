package io.github.yb.server.repositorios

import io.github.yb.server.entidades.CarreraUsuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Repository
interface RepositorioCarreraUsuario : JpaRepository<CarreraUsuario, String> {

    @Query("SELECT * FROM carrerasusuario WHERE username = :usuario", nativeQuery = true)
    fun verPorUsuario(@Param("usuario") usuario: String): List<CarreraUsuario>

    @Modifying
    @Transactional
    @Query(
        value = """
        INSERT INTO carrerasusuario (id, carrera, username, creado_el)
        VALUES (:id, :carrera, :username, :creadoEl)
    """, nativeQuery = true
    )
    fun crear(
        @Param("id") id: String,
        @Param("carrera") carrera: String,
        @Param("username") username: String,
        @Param("creadoEl") creadoEl: OffsetDateTime
    )

    fun crear(carreraUsuario: CarreraUsuario): CarreraUsuario {
        crear(
            id = carreraUsuario.id,
            carrera = carreraUsuario.carrera,
            username = carreraUsuario.username,
            creadoEl = carreraUsuario.creadoEl
        )
        return carreraUsuario
    }
}
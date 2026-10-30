package io.github.yb.server.repositorios

import io.github.yb.server.entidades.MateriaUsuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Repository
interface RepositorioMateriaUsuario : JpaRepository<MateriaUsuario, String> {

    @Query("SELECT * FROM materiasusuario WHERE id_usuario = :usuario", nativeQuery = true)
    fun verPorUsuario(@Param("usuario") usuario: String): List<MateriaUsuario>

    @Query("""
        SELECT mu.* FROM materiasusuario mu
        INNER JOIN usuarios u ON mu.id_usuario = u.username
        WHERE u.universidad = :universidad
    """, nativeQuery = true)
    fun verPorUniversidad(@Param("universidad") universidad: String): List<MateriaUsuario>

    @Modifying
    @Transactional
    @Query(
        value = """
        INSERT INTO materiasusuario (id, id_materia, id_usuario, creado_el)
        VALUES (:id, :idMateria, :idUsuario, :creadoEl)
    """, nativeQuery = true
    )
    fun crear(
        @Param("id") id: String,
        @Param("idMateria") idMateria: String,
        @Param("idUsuario") idUsuario: String,
        @Param("creadoEl") creadoEl: OffsetDateTime
    )

    fun crear(materiaUsuario: MateriaUsuario): MateriaUsuario{
        crear(
            id = materiaUsuario.id,
            idMateria = materiaUsuario.idMateria,
            idUsuario = materiaUsuario.idUsuario,
            creadoEl = materiaUsuario.creadoEl
        )
        return materiaUsuario
    }
}
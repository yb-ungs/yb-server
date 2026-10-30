package io.github.yb.server.repositorios

import io.github.yb.server.entidades.AulaHorario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Repository
interface RepositorioAulaHorario: JpaRepository<AulaHorario, String> {

    @Query("""
        SELECT * FROM aulashorarios
        WHERE comision = :comision
        AND id_materia = :idMateria
        AND universidad = :universidad
        """, nativeQuery = true)
    fun verPorComisionPorMateriaPorUniversidad(
        @Param("comision") comision: String,
        @Param("idMateria") idMateria: String,
        @Param("universidad") universidad: String,
    ): List<AulaHorario>

    @Modifying
    @Transactional
    @Query(
        value = """
        INSERT INTO aulashorarios (id, id_materia, id_aula, comision, horario, universidad, creado_el)
        VALUES (:id, :idMateria, :idAula, :comision, :horario, :universidad, :creadoEl)
    """, nativeQuery = true
    )
    fun crear(
        @Param("id") id: String,
        @Param("idMateria") idMateria: String,
        @Param("idAula") idAula: String,
        @Param("comision") comision: String,
        @Param("horario") horario: String,
        @Param("universidad") universidad: String,
        @Param("creadoEl") creadoEl: OffsetDateTime
    )

    fun crear(aulaHorario: AulaHorario): AulaHorario {
        crear(
            id = aulaHorario.id,
            idMateria = aulaHorario.idMateria,
            idAula = aulaHorario.idAula,
            comision = aulaHorario.comision,
            horario = aulaHorario.horario,
            universidad = aulaHorario.universidad,
            creadoEl = aulaHorario.creadoEl
        )
        return aulaHorario
    }

    @Modifying
    @Transactional
    @Query(
        value = """
        UPDATE aulashorarios
        SET id_materia = :idMateria,
            id_aula = :idAula,
            comision = :comision,
            horario = :horario,
            universidad = :universidad,
            creado_el = :creadoEl
        WHERE id = :id
    """, nativeQuery = true
    )
    fun actualizar(
        @Param("id") id: String,
        @Param("idMateria") idMateria: String,
        @Param("idAula") idAula: String,
        @Param("comision") comision: String,
        @Param("horario") horario: String,
        @Param("universidad") universidad: String,
        @Param("creadoEl") creadoEl: OffsetDateTime
    )

    fun actualizar(aulaHorario: AulaHorario) {
        actualizar(
            id = aulaHorario.id,
            idMateria = aulaHorario.idMateria,
            idAula = aulaHorario.idAula,
            comision = aulaHorario.comision,
            horario = aulaHorario.horario,
            universidad = aulaHorario.universidad,
            creadoEl = aulaHorario.creadoEl
        )
    }
}
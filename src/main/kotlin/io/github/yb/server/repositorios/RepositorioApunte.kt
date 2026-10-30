package io.github.yb.server.repositorios

import io.github.yb.server.entidades.Apunte
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Repository
interface RepositorioApunte : JpaRepository<Apunte, String> {

    @Query("SELECT * FROM apuntes WHERE universidad = :universidad", nativeQuery = true)
    fun encontrarPorUniversidad(@Param("universidad") universidad: String): List<Apunte>

    @Query(
        """
        SELECT * FROM apuntes
        WHERE universidad = :universidad
        AND id_materia = :idMateria
    """, nativeQuery = true
    )
    fun encontrarPorMateriaPorUniversidad(
        @Param("idMateria") idMateria: String,
        @Param("universidad") universidad: String
    ): List<Apunte>

    @Modifying
    @Transactional
    @Query(
        value = """
        INSERT INTO apuntes (id, id_apunte, id_materia, nombre, descripcion, ubicacion, universidad, creado_el)
        VALUES (:id, :idApunte, :idMateria, :nombre, :descripcion, :ubicacion, :universidad, :creadoEl)
    """, nativeQuery = true
    )
    fun crear(
        @Param("id") id: String,
        @Param("idApunte") idApunte: String,
        @Param("idMateria") idMateria: String,
        @Param("nombre") nombre: String,
        @Param("descripcion") descripcion: String,
        @Param("ubicacion") ubicacion: String,
        @Param("universidad") universidad: String,
        @Param("creadoEl") creadoEl: OffsetDateTime
    )

    fun crear(apunte: Apunte): Apunte{
        crear(
            id = apunte.id,
            idApunte = apunte.idApunte,
            idMateria = apunte.idMateria,
            nombre = apunte.nombre,
            descripcion = apunte.descripcion,
            ubicacion = apunte.ubicacion,
            universidad = apunte.universidad,
            creadoEl = apunte.creadoEl
        )
        return apunte
    }

    @Modifying
    @Transactional
    @Query(
        value = """
        UPDATE apuntes
        SET id_apunte = :idApunte,
            id_materia = :idMateria,
            nombre = :nombre,
            descripcion = :descripcion,
            ubicacion = :ubicacion,
            universidad = :universidad,
            creado_el = :creadoEl
        WHERE id = :id
    """, nativeQuery = true
    )
    fun actualizar(
        @Param("id") id: String,
        @Param("idApunte") idApunte: String,
        @Param("idMateria") idMateria: String,
        @Param("nombre") nombre: String,
        @Param("descripcion") descripcion: String,
        @Param("ubicacion") ubicacion: String,
        @Param("universidad") universidad: String,
        @Param("creadoEl") creadoEl: OffsetDateTime
    )

    fun actualizar(apunte: Apunte) {
        actualizar(
            id = apunte.id,
            idApunte = apunte.idApunte,
            idMateria = apunte.idMateria,
            nombre = apunte.nombre,
            descripcion = apunte.descripcion,
            ubicacion = apunte.ubicacion,
            universidad = apunte.universidad,
            creadoEl = apunte.creadoEl
        )
    }
}
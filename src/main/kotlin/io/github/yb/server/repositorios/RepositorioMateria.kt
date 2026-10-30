package io.github.yb.server.repositorios

import io.github.yb.server.entidades.Materia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface RepositorioMateria : JpaRepository<Materia, String> {

    @Query("SELECT * FROM materias WHERE id_materia = :idMateria AND universidad = :universidad", nativeQuery = true)
    fun verPorIdPorUniversidad(
        @Param("idMateria") idMateria: String,
        @Param("universidad") universidad: String
    ): List<Materia>


    @Query("SELECT * FROM materias WHERE universidad = :universidad", nativeQuery = true)
    fun verPorUniversidad(universidad: String): List<Materia>

    @Modifying
    @Transactional
    @Query(
        value = """
        INSERT INTO materias (id, id_materia, nombre, universidad)
        VALUES (:id, :idMateria, :nombre, :universidad)
    """, nativeQuery = true
    )
    fun crear(
        @Param("id") id: String,
        @Param("idMateria") idMateria: String,
        @Param("nombre") nombre: String,
        @Param("universidad") universidad: String
    )

    fun crear(materia: Materia): Materia {
        crear(
            id = materia.id,
            idMateria = materia.idMateria,
            nombre = materia.nombre,
            universidad = materia.universidad
        )
        return materia
    }

    @Modifying
    @Transactional
    @Query(
        value = """
        UPDATE materias
        SET id_materia = :idMateria,
            nombre = :nombre,
            universidad = :universidad
        WHERE id = :id
    """, nativeQuery = true
    )
    fun actualizar(
        @Param("id") id: String,
        @Param("idMateria") idMateria: String,
        @Param("nombre") nombre: String,
        @Param("universidad") universidad: String
    )

    fun actualizar(materia: Materia) {
        actualizar(
            id = materia.id,
            idMateria = materia.idMateria,
            nombre = materia.nombre,
            universidad = materia.universidad
        )
    }

}
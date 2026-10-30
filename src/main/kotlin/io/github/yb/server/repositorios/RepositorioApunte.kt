package io.github.yb.server.repositorios

import io.github.yb.server.entidades.Apunte
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RepositorioApuntes : JpaRepository<Apunte, String> {

    @Query("SELECT * FROM apuntes WHERE universidad = :universidad", nativeQuery = true)
    fun encontrarPorUniversidad(@Param("universidad") universidad: String): List<Apunte>

    @Query(
        """
        SELECT * FROM apuntes 
        WHERE universidad = :universidad
        AND id_materia = :idMateria
    """, nativeQuery = true
    )
    fun encontrarPorMateriaPorUniversidad(@Param("idMateria") idMateria: String): List<Apunte>
}
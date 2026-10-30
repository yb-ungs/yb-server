package io.github.yb.server.repositorios

import io.github.yb.server.entidades.MateriaPlan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface RepositorioMateriaPlan : JpaRepository<MateriaPlan, String> {

    @Query("SELECT * FROM materiasplan WHERE universidad = :universidad", nativeQuery = true)
    fun verPorUniversidad(
        @Param("universidad") universidad: String
    ): List<MateriaPlan>

    @Query("SELECT * FROM materiasplan WHERE id_materia = :idMateria", nativeQuery = true)
    fun verPorMateria(@Param("idMateria") idMateria: String): List<MateriaPlan>

    @Modifying
    @Transactional
    @Query(
        value = """
        INSERT INTO materiasplan (id, id_materia, correlativas, id_plan, cuatrimestre, carrera, plan, universidad)
        VALUES (:id, :idMateria, :correlativas, :idPlan, :cuatrimestre, :carrera, :plan, :universidad)
    """, nativeQuery = true
    )
    fun crear(
        @Param("id") id: String,
        @Param("idMateria") idMateria: String,
        @Param("correlativas") correlativas: String,
        @Param("idPlan") idPlan: String,
        @Param("cuatrimestre") cuatrimestre: String,
        @Param("carrera") carrera: String,
        @Param("plan") plan: String,
        @Param("universidad") universidad: String
    )

    fun crear(materiaPlan: MateriaPlan): MateriaPlan {
        crear(
            id = materiaPlan.id,
            idMateria = materiaPlan.idMateria,
            correlativas = materiaPlan.correlativas,
            idPlan = materiaPlan.idPlan,
            cuatrimestre = materiaPlan.cuatrimestre,
            carrera = materiaPlan.carrera,
            plan = materiaPlan.plan,
            universidad = materiaPlan.universidad
        )
        return materiaPlan
    }

    @Modifying
    @Transactional
    @Query(
        value = """
        UPDATE materiasplan
        SET id_materia = :idMateria,
            correlativas = :correlativas,
            id_plan = :idPlan,
            cuatrimestre = :cuatrimestre,
            carrera = :carrera,
            plan = :plan,
            universidad = :universidad
        WHERE id = :id
    """, nativeQuery = true
    )
    fun actualizar(
        @Param("id") id: String,
        @Param("idMateria") idMateria: String,
        @Param("correlativas") correlativas: String,
        @Param("idPlan") idPlan: String,
        @Param("cuatrimestre") cuatrimestre: String,
        @Param("carrera") carrera: String,
        @Param("plan") plan: String,
        @Param("universidad") universidad: String
    )

    fun actualizar(materiaPlan: MateriaPlan) {
        actualizar(
            id = materiaPlan.id,
            idMateria = materiaPlan.idMateria,
            correlativas = materiaPlan.correlativas,
            idPlan = materiaPlan.idPlan,
            cuatrimestre = materiaPlan.cuatrimestre,
            carrera = materiaPlan.carrera,
            plan = materiaPlan.plan,
            universidad = materiaPlan.universidad
        )
    }
}
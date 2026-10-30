package io.github.yb.server.repositorios

import io.github.yb.server.entidades.Registro
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Repository
interface RepositorioRegistro : JpaRepository<Registro, String> {

    @Query("SELECT * FROM registros WHERE creado_el >= :desde AND creado_el < :hasta", nativeQuery = true)
    fun verPorCursores(
        @Param("desde") desde: OffsetDateTime,
        @Param("hasta") hasta: OffsetDateTime
    ): List<Registro>

    @Query("SELECT * FROM registros WHERE nivel = :nivel AND creado_el >= :desde AND creado_el < :hasta", nativeQuery = true)
    fun verPorNivelConCursores(
        @Param("desde") desde: OffsetDateTime,
        @Param("hasta") hasta: OffsetDateTime,
        @Param("nivel") nivel: String
    ): List<Registro>

    @Query("SELECT * FROM registros WHERE nombre = :nombre AND nivel = :nivel AND creado_el >= :desde AND creado_el < :hasta", nativeQuery = true)
    fun verPorNivelPorNombreConCursores(
        @Param("desde") desde: OffsetDateTime,
        @Param("hasta") hasta: OffsetDateTime,
        @Param("nivel") nivel: String,
        @Param("nombre") nombre: String
    ): List<Registro>
}
package io.github.yb.server.repositorios

import io.github.yb.server.entidades.Anuncio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Repository
interface RepositorioAnuncio : JpaRepository<Anuncio, String> {

    @Query(
        value = "SELECT * FROM anuncios WHERE universidad = :universidad ORDER BY anuncios.creado_el DESC LIMIT 10",
        nativeQuery = true
    )
    fun encontrarUltimosAnunciosUniversidad(@Param("universidad") universidad: String): List<Anuncio>

    @Query(
        value = """
        SELECT * FROM anuncios
        WHERE autor       ILIKE '%' || :str || '%'
           OR titulo      ILIKE '%' || :str || '%'
           OR contenido   ILIKE '%' || :str || '%'
           OR agrupacion  ILIKE '%' || :str || '%'
           AND universidad = :universidad
    """, nativeQuery = true
    )
    fun encontrarCualquieraPorUniversidad(
        @Param("str") str: String,
        @Param("universidad") universidad: String
    ): List<Anuncio>

    @Query(
        value = """
        SELECT * FROM anuncios
        WHERE autor       ILIKE '%' || :str || '%'
           OR titulo      ILIKE '%' || :str || '%'
           OR contenido   ILIKE '%' || :str || '%'
           OR agrupacion  ILIKE '%' || :str || '%'
           AND universidad = :universidad
           AND creado_el >= :desde
           AND creado_el < :hasta
    """, nativeQuery = true
    )
    fun encontrarCualquieraPorUniversidadConCursores(
        @Param("str") str: String,
        @Param("universidad") universidad: String,
        @Param("desde") desde: OffsetDateTime,
        @Param("hasta") hasta: OffsetDateTime
    ): List<Anuncio>

    @Query(
        value = """
        SELECT * FROM anuncios
            WHERE universidad = :universidad
            AND creado_el >= :desde
            AND creado_el < :hasta
    """, nativeQuery = true
    )
    fun encontrarPorUniversidadConCursores(
        @Param("universidad") universidad: String,
        @Param("desde") desde: OffsetDateTime,
        @Param("hasta") hasta: OffsetDateTime
    ): List<Anuncio>

    @Modifying
    @Transactional
    @Query(
        value = """
        INSERT INTO anuncios (id, autor, titulo, contenido, universidad, agrupacion, creado_el)
        VALUES (:id, :autor, :titulo, :contenido, :universidad, :agrupacion, :creadoEl)
    """, nativeQuery = true
    )
    fun crear(
        @Param("id") id: String,
        @Param("autor") autor: String,
        @Param("titulo") titulo: String,
        @Param("contenido") contenido: String,
        @Param("universidad") universidad: String,
        @Param("agrupacion") agrupacion: String,
        @Param("creadoEl") creadoEl: OffsetDateTime
    )

    fun crear(anuncio: Anuncio): Anuncio {
        crear(
            id = anuncio.id,
            autor = anuncio.autor,
            titulo = anuncio.titulo,
            contenido = anuncio.contenido,
            universidad = anuncio.universidad,
            agrupacion = anuncio.agrupacion,
            creadoEl = anuncio.creadoEl
        )
        return anuncio
    }

    @Modifying
    @Transactional
    @Query(
        value = """
        UPDATE anuncios
        SET autor = :autor,
            titulo = :titulo,
            contenido = :contenido,
            universidad = :universidad,
            agrupacion = :agrupacion,
            creado_el = :creadoEl
        WHERE id = :id
    """, nativeQuery = true
    )
    fun actualizar(
        @Param("id") id: String,
        @Param("autor") autor: String,
        @Param("titulo") titulo: String,
        @Param("contenido") contenido: String,
        @Param("universidad") universidad: String,
        @Param("agrupacion") agrupacion: String,
        @Param("creadoEl") creadoEl: OffsetDateTime
    )

    fun actualizar(anuncio: Anuncio): Anuncio {
        actualizar(
            id = anuncio.id,
            autor = anuncio.autor,
            titulo = anuncio.titulo,
            contenido = anuncio.contenido,
            universidad = anuncio.universidad,
            agrupacion = anuncio.agrupacion,
            creadoEl = anuncio.creadoEl
        )
        return anuncio
    }
}
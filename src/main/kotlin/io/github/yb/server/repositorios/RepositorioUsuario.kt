package io.github.yb.server.repositorios

import io.github.yb.server.entidades.Usuario
import io.github.yb.server.modelos.DatosUsuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RepositorioUsuario : JpaRepository<Usuario, String> {
    @Query(value = "SELECT * FROM usuarios WHERE username = :usuario", nativeQuery = true)
    fun encontrarPorUsuario(@Param("usuario") usuario: String): Usuario?

    @Query(value = "SELECT * FROM usuarios WHERE universidad = :universidad", nativeQuery = true)
    fun encontrarPorUniversidad(@Param("universidad") universidad: String): List<DatosUsuario>

}
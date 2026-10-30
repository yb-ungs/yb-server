package io.github.yb.server.servicios

import io.github.yb.server.entidades.Registro
import io.github.yb.server.modelos.ModeloRegistro
import io.github.yb.server.repositorios.RepositorioRegistro
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class ServicioRegistro(val repositorioRegistro: RepositorioRegistro) {

    private fun Registro.modelo(): ModeloRegistro {
        return ModeloRegistro(
            id,
            creadoEl.toString(),
            nivel,
            nombre,
            hilo,
            mensaje,
            traza
        )
    }

    fun encontrarPorCursores(desde: String, hasta: String): List<ModeloRegistro> {
        return repositorioRegistro
            .verPorCursores(
                OffsetDateTime.parse(desde),
                OffsetDateTime.parse(hasta)
            )
            .map { it.modelo() }
    }

    fun encontrarPorNivelConCursores(nivel: String, desde: String, hasta: String): List<ModeloRegistro> {
        return repositorioRegistro
            .verPorNivelConCursores(
                OffsetDateTime.parse(desde),
                OffsetDateTime.parse(hasta),
                nivel
            )
            .map { it.modelo() }
    }

    fun encontrarPorNivelPorNombreConCursores(
        nivel: String,
        nombre: String,
        desde: String,
        hasta: String
    ): List<ModeloRegistro> {
        return repositorioRegistro
            .verPorNivelPorNombreConCursores(
                OffsetDateTime.parse(desde),
                OffsetDateTime.parse(hasta),
                nivel,
                nombre
            )
            .map { it.modelo() }
    }
}
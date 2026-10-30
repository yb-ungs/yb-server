package io.github.yb.server.servicios

import com.github.f4b6a3.uuid.UuidCreator
import io.github.yb.server.entidades.Anuncio
import io.github.yb.server.modelos.DatosAnuncio
import io.github.yb.server.modelos.NuevoAnuncio
import io.github.yb.server.repositorios.RepositorioAnuncio
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class ServicioAnuncio(
    val repositorioAnuncio: RepositorioAnuncio,
    val servicioContextoSeguridad: ServicioContextoSeguridad
) {

    private fun Anuncio.modelo(): DatosAnuncio {
        return DatosAnuncio(
            id = id,
            universidad = universidad,
            titulo = titulo,
            contenido = contenido,
            agrupacion = agrupacion,
            autor = autor,
            creadoEl = creadoEl.toString()
        )
    }

    private fun DatosAnuncio.entidad(): Anuncio {
        return Anuncio(
            id = id,
            universidad = universidad,
            titulo = titulo,
            contenido = contenido,
            agrupacion = agrupacion,
            autor = autor,
            creadoEl = creadoEl.let{ OffsetDateTime.parse(it) }
        )
    }

    private fun NuevoAnuncio.anuncio(universidad: String): Anuncio {
        val creadoEl = OffsetDateTime.now()
        val id = UuidCreator.getTimeOrderedEpoch().toString()
        return Anuncio(
            id,
            autor,
            titulo,
            contenido,
            universidad,
            agrupacion,
            creadoEl
        )
    }

    fun encontrarUltimosUniversidad(): List<DatosAnuncio> {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return encontrarUltimosAnunciosPorUniversidad(datosUsuario.universidad)
    }

    @Cacheable("anuncio_ultimos_por_universidad", key = "#universidad + '_' + #idMateria")
    fun encontrarUltimosAnunciosPorUniversidad(universidad: String) : List<DatosAnuncio> {
        return repositorioAnuncio.encontrarUltimosAnunciosUniversidad(universidad).map { it.modelo() }
    }

    fun encontrarMiUniversidad(str: String): List<DatosAnuncio> {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return repositorioAnuncio.encontrarCualquieraPorUniversidad(str, datosUsuario.universidad).map { it.modelo() }
    }

    fun encontrarPorCursorMiUniversidad(str: String, desde: OffsetDateTime, hasta: OffsetDateTime): List<DatosAnuncio> {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return repositorioAnuncio.encontrarCualquieraPorUniversidadConCursores(
            str,
            datosUsuario.universidad,
            desde,
            hasta
        ).map { it.modelo() }
    }

    fun guardar(nuevoAnuncio: NuevoAnuncio): DatosAnuncio{
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return repositorioAnuncio.crear(nuevoAnuncio.anuncio(datosUsuario.universidad)).modelo()
    }

    fun actualizar(datosAnuncio: DatosAnuncio): DatosAnuncio {
        return repositorioAnuncio.actualizar(datosAnuncio.entidad()).modelo()
    }

    fun borrar(id: String) {
        repositorioAnuncio.deleteById(id)
    }

    fun guardar(nuevoAnuncio: NuevoAnuncio, universidad: String): DatosAnuncio {
        return repositorioAnuncio.crear(nuevoAnuncio.anuncio(universidad)).modelo()
    }

    fun encontrarPorUniversidadConCursores(
        universidad: String,
        desde: String,
        hasta: String
    ): List<DatosAnuncio> {
        return repositorioAnuncio.encontrarPorUniversidadConCursores(
            universidad,
            OffsetDateTime.parse(desde),
            OffsetDateTime.parse(hasta)
        ).map{ it.modelo() }
    }

    fun encontrarPorCursoresMiUniversidad(
        desde: String,
        hasta: String
    ): List<DatosAnuncio> {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return repositorioAnuncio.encontrarPorUniversidadConCursores(
            datosUsuario.universidad,
            OffsetDateTime.parse(desde),
            OffsetDateTime.parse(hasta)
        ).map{ it.modelo() }
    }


}
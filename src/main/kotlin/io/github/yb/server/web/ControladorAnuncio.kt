package io.github.yb.server.web

import io.github.yb.server.modelos.DatosAnuncio
import io.github.yb.server.modelos.NuevoAnuncio
import io.github.yb.server.servicios.ServicioAnuncio
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.OffsetDateTime

@RestController
@RequestMapping("/v1/anuncio")
class ControladorAnuncio(
    val servicioAnuncio: ServicioAnuncio
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ControladorAnuncio::class.java)
    }

    @Timed("anuncio.ultimos.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerAnunciosSuUniversidad()")
    @GetMapping("/ultimos-mi-universidad")
    fun ultimosMiUniversidad(): ResponseEntity<List<DatosAnuncio>> {
        logger.trace("entrado a endpoint ultimos-mi-universidad")
        return servicioAnuncio.encontrarUltimosUniversidad().let {
            logger.trace("ultimos-mi-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("anuncio.encontrar.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerAnunciosSuUniversidad()")
    @GetMapping("/encontrar-cualquiera-mi-universidad")
    fun encontrarMiUniversidad(
        @RequestParam("str") str: String
    ): ResponseEntity<List<DatosAnuncio>> {
        logger.trace("entrado a endpoint encontrar-cualquiera-mi-universidad")
        return servicioAnuncio.encontrarMiUniversidad(str).let {
            logger.trace("encontrar-cualquiera-mi-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("anuncio.encontrar.cualquiera.por.cursor.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerAnunciosSuUniversidad()")
    @GetMapping("/encontrar-cualquiera-por-cursor-mi-universidad")
    fun encontrarCualquieraPorCursorMiUniversidad(
        @RequestParam("str") str: String,
        @RequestParam("desde") desde: String,
        @RequestParam("hasta") hasta: String
    ): ResponseEntity<List<DatosAnuncio>> {
        logger.trace("entrado a endpoint encontrar-cualquiera-por-cursor-mi-universidad")
        return servicioAnuncio.encontrarPorCursorMiUniversidad(
            str,
            desde.let { OffsetDateTime.parse(it) },
            hasta.let { OffsetDateTime.parse(it) }
        ).let {
            logger.trace("encontrar-cualquiera-por-cursor-mi-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("anuncio.crear.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerAnunciosSuUniversidad()")
    @PostMapping("/crear-mi-universidad")
    fun crearMiUniversidad(@RequestBody nuevoAnuncio: NuevoAnuncio): ResponseEntity<String> {
        logger.trace("entrado a endpoint crear-mi-universidad")
        return servicioAnuncio.guardar(nuevoAnuncio).let {
            ResponseEntity.ok("Anuncio guardado con id ${it.id}.".also { msg ->
                logger.trace("crear-mi-universidad devuelve {}", msg)
            })
        }
    }

    @Timed("anuncio.actualizar.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeEditarAnunciosSuUniversidad(#datosAnuncio)")
    @PatchMapping("/actualizar-mi-universidad")
    fun actualizarMiUniversidad(@RequestBody datosAnuncio: DatosAnuncio): ResponseEntity<String> {
        logger.trace("actualizar-mi-universidad")
        return servicioAnuncio.actualizar(datosAnuncio).let {
            ResponseEntity.ok("Se actualizo el anuncio con id ${it.id}.".also { msg ->
                logger.trace("actualizar-mi-universidad devuelve {}", msg)
            })
        }
    }

    @Timed("anuncio.borrar.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarAnunciosSuUniversidad(#id)")
    @DeleteMapping("/borrar-mi-universidad")
    fun borrarMiUniversidad(@RequestParam("id") id: String): ResponseEntity<String> {
        logger.trace("ingresando a endpoint borrar-mi-universidad")
        servicioAnuncio.borrar(id)
        return ResponseEntity.ok("Se borro el anuncio con id $id.".also { msg ->
            logger.trace("borrar-mi-universidad devuelve {}", msg)
        })
    }

    @Timed("anuncio.borrar")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarAnuncio()")
    @DeleteMapping("/borrar")
    fun borrar(@RequestParam("id") id: String): ResponseEntity<String> {
        logger.trace("ingresando a endpoint borrar")
        servicioAnuncio.borrar(id)
        return ResponseEntity.ok("Se borro el anuncio con id $id.").also { msg ->
            logger.trace("borrar devuelve {}", msg)
        }
    }

    @Timed("anuncio.crear")
    @PreAuthorize("@servicioAutorizacion.puedeCrearAnuncio()")
    @PostMapping("/crear")
    fun crear(
        @RequestBody nuevoAnuncio: NuevoAnuncio,
        @RequestParam("universidad") universidad: String
    ): ResponseEntity<String> {
        logger.trace("ingresando a endpoint crear")
        return servicioAnuncio.guardar(nuevoAnuncio, universidad).let {
            ResponseEntity.ok("Anuncio guardado con id ${it.id}.").also { msg ->
                logger.trace("crear devuelve {}", msg)
            }
        }
    }

    @Timed("anuncio.actualizar")
    @PreAuthorize("@servicioAutorizacion.puedeModificarAnuncio()")
    @PatchMapping("/actualizar")
    fun actualizar(@RequestBody datosAnuncio: DatosAnuncio): ResponseEntity<String> {
        logger.trace("ingresando a endpoint actualizar")
        return servicioAnuncio.actualizar(datosAnuncio).let {
            ResponseEntity.ok("Se actualizo el anuncio con id ${it.id}.").also { msg ->
                logger.trace("actualizar devuelve {}", msg)
            }
        }
    }

    @Timed("anuncio.encontrar.por.universidad.con.cursores")
    @PreAuthorize("@servicioAutorizacion.puedeEncontrarAnunciosPorCursor()")
    @GetMapping("/encontrar-por-universidad-con-cursores")
    fun encontrarPorUniversidadConCursores(
        @RequestParam("universidad") universidad: String,
        @RequestParam("desde") desde: String,
        @RequestParam("hasta") hasta: String
    ): ResponseEntity<List<DatosAnuncio>> {
        return servicioAnuncio.encontrarPorUniversidadConCursores(universidad, desde, hasta).let {
            logger.trace("entrando a encontrar-por-universidad-con-cursores")
            ResponseEntity.ok(it.also { elementos ->
                logger.trace("encontrar-por-universidad-con-cursores devuelve {} filas", elementos.size)
            })
        }
    }

    @Timed("anuncio.encontrar.por.cursores.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerAnunciosSuUniversidad()")
    @GetMapping("/encontrar-por-cursores-mi-universidad")
    fun encontrarPorCursoresMiUniversidad(
        @RequestParam("desde") desde: String,
        @RequestParam("hasta") hasta: String
    ): ResponseEntity<List<DatosAnuncio>> {
        logger.trace("entrando a encontrar-por-cursores-mi-universidad")
        return servicioAnuncio.encontrarPorCursoresMiUniversidad(desde, hasta).let {
            ResponseEntity.ok(it.also { elementos ->
                logger.trace("encontrar-por-cursores-mi-universidad devuelve {} filas", elementos.size)
            })
        }
    }
}
package io.github.yb.server.web

import io.github.yb.server.modelos.ModeloRegistro
import io.github.yb.server.servicios.ServicioAutorizacion
import io.github.yb.server.servicios.ServicioRegistro
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/registro")
class ControladorRegistro(val servicioRegistro: ServicioRegistro) {

    companion object {
        private val logger = LoggerFactory.getLogger(ControladorRegistro::class.java)
    }

    @Timed("registro.ver.por.cursores")
    @PreAuthorize("@servicioAutorizacion.puedeVerRegistros()")
    @GetMapping("/ver-por-cursores")
    fun verPorCursores(
        @RequestParam("desde") desde: String,
        @RequestParam("hasta") hasta: String
    ): ResponseEntity<List<ModeloRegistro>> {
        logger.trace("entrado a endpoint ver-por-cursores")
        return servicioRegistro.encontrarPorCursores(desde, hasta).let {
            logger.trace("ver-por-cursores devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("registro.ver.por.nivel.con.cursores")
    @PreAuthorize("@servicioAutorizacion.puedeVerRegistros()")
    @GetMapping("/ver-por-nivel-con-cursores")
    fun verPorNivelConCursores(
        @RequestParam("desde") desde: String,
        @RequestParam("hasta") hasta: String,
        @RequestParam("nivel") nivel: String
    ): ResponseEntity<List<ModeloRegistro>> {
        logger.trace("entrado a endpoint ver-por-nivel-con-cursores")
        return servicioRegistro.encontrarPorNivelConCursores(desde, hasta, nivel)
            .let {
                logger.trace("ver-por-nivel-con-cursores devuelve {} filas", it.size)
                ResponseEntity.ok(it)
            }
    }

    @Timed("registro.ver.por.nivel.por.nombre.con.cursores")
    @PreAuthorize("@servicioAutorizacion.puedeVerRegistros()")
    @GetMapping("/ver-por-nivel-por-nombre-con-cursores")
    fun verPorNivelPorNombreConCursores(
        @RequestParam("desde") desde: String,
        @RequestParam("hasta") hasta: String,
        @RequestParam("nivel") nivel: String,
        @RequestParam("nombre") nombre: String
    ): ResponseEntity<List<ModeloRegistro>> {
        logger.trace("entrado a endpoint ver-por-nivel-por-nombre-con-cursores")
        return servicioRegistro.encontrarPorNivelPorNombreConCursores(
            desde,
            hasta,
            nivel,
            nombre
        ).let {
            logger.trace("ver-por-nivel-por-nombre-con-cursores devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

}
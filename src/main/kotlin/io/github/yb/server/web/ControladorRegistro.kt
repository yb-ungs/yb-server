package io.github.yb.server.web

import io.github.yb.server.modelos.ModeloRegistro
import io.github.yb.server.servicios.ServicioRegistro
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/registro")
class ControladorRegistros(val servicioRegistro: ServicioRegistro) {

    @PreAuthorize("@servicioAutorizacion.puedeVerRegistros()")
    @GetMapping("/ver-por-cursores")
    fun verPorCursores(
        @RequestParam("desde") desde: String,
        @RequestParam("hasta") hasta: String
    ): ResponseEntity<List<ModeloRegistro>> {
        return servicioRegistro.encontrarPorCursores(desde, hasta).let { ResponseEntity.ok(it) }
    }

    @PreAuthorize("@servicioAutorizacion.verPorNivelConCursores()")
    @GetMapping("/ver-por-nivel-con-cursores")
    fun verPorNivelConCursores(
        @RequestParam("desde") desde: String,
        @RequestParam("hasta") hasta: String,
        @RequestParam("nivel") nivel: String
    ): ResponseEntity<List<ModeloRegistro>> {
        return servicioRegistro.encontrarPorNivelConCursores(desde, hasta, nivel)
            .let { ResponseEntity.ok(it) }
    }

    @PreAuthorize("@servicioAutorizacion.verPorNivelConCursores()")
    @GetMapping("/ver-por-nivel-por-nombre-con-cursores")
    fun verPorNivelPorNombreConCursores(
        @RequestParam("desde") desde: String,
        @RequestParam("hasta") hasta: String,
        @RequestParam("nivel") nivel: String,
        @RequestParam("nombre") nombre: String
    ): ResponseEntity<List<ModeloRegistro>> {
        return servicioRegistro.encontrarPorNivelPorNombreConCursores(
            desde,
            hasta,
            nivel,
            nombre
        ).let { ResponseEntity.ok(it) }
    }

}
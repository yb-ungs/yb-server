package io.github.yb.server.web

import io.github.yb.server.modelos.DatosAulaHorario
import io.github.yb.server.modelos.NuevoAulaHorario
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/aula-horario")
class ControladorAulaHorario {

    @PreAuthorize("@servicioAutorizacion.puedeVerAulaHorarioSuUniversidad()")
    @GetMapping("/ver-por-comision-por-materia-mi-universidad")
    fun verPorComisionPorMateriaMiUniversidad(
        @RequestParam("comision") comision: String,
        @RequestParam("materia") materia: String
    ){
        TODO()
    }

    @PreAuthorize("@servicioAutorizacion.puedeVerAulaHorario()")
    @GetMapping("/ver-por-comision-por-materia-por-universidad")
    fun verPorComisionPorMateriaPorUniversidad(
        @RequestParam("comision") comision: String,
        @RequestParam("materia") materia: String,
        @RequestParam("universidad") universidad: String
    ){
        TODO()
    }

    @PreAuthorize("@servicioAutorizacion.puedeCrearAulaHorarioSuUniversidad(#nuevoAulaHorario)")
    @PostMapping("/crear-mi-universidad")
    fun crearMiUniversidad(
        @RequestBody nuevoAulaHorario: NuevoAulaHorario
    ){
        TODO()
    }

    @PreAuthorize("@servicioAutorizacion.puedeActualizarAulaHorarioSuUniversidad(#datosAulaHorario)")
    @PatchMapping("/actualizar-mi-universidad")
    fun actualizarMiUniversidad(
        @RequestBody datosAulaHorario: DatosAulaHorario
    ){
        TODO()
    }

    @PreAuthorize("@servicioAutorizacion.puedeBorrarAulaHorarioSuUniversidad(#id)")
    @DeleteMapping("/borrar-mi-universidad")
    fun borrarMiUniversidad(
        @RequestParam("id") id: String
    ){
        TODO()
    }

    @PostMapping("/crear")
    fun crear(
        @RequestBody nuevoAulaHorario: NuevoAulaHorario
    ){
        TODO()
    }

    @PatchMapping("/actualizar")
    fun actualizar(
        @RequestBody datosAulaHorario: DatosAulaHorario
    ){
        TODO()
    }

    @DeleteMapping("/borrar")
    fun borrar(
        @RequestParam("id") id: String
    ){
        TODO()
    }
}
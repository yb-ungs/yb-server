package io.github.yb.server.web

import io.github.yb.server.modelos.DatosMateriaPlan
import io.github.yb.server.modelos.NuevoMateriaPlan
import io.github.yb.server.servicios.ServicioAutorizacion
import io.github.yb.server.servicios.ServicioMateriaPlan
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
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
@RequestMapping("/v1/materia-plan")
class ControladorMateriaPlan(
    val servicioMateriaPlan: ServicioMateriaPlan
) {

    companion object {
        private val logger = LoggerFactory.getLogger(ControladorMateriaPlan::class.java)
    }

    @Timed("materia.plan.crear.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeCrearMateriaPlanSuUniversidad()")
    @PostMapping("/crear-mi-universidad")
    fun crearMiUniversidad(@RequestBody nuevoMateriaPlan: NuevoMateriaPlan): ResponseEntity<String> {
        logger.trace("entrado a endpoint crear-mi-universidad")
        return servicioMateriaPlan.crear(nuevoMateriaPlan).let {
            ResponseEntity.ok("Materia ${it.idMateria} agregada al plan de la carrera ${it.carrera} correctamente.".also { msg ->
                logger.trace("crear-mi-universidad devuelve {}", msg)
            })
        }
    }

    @Timed("materia.plan.crear")
    @PreAuthorize("@servicioAutorizacion.puedeCrearMateriaPlan()")
    @PostMapping("/crear")
    fun crear(@RequestBody datosMateriaPlan: DatosMateriaPlan): ResponseEntity<String> {
        logger.trace("entrado a endpoint crear")
        servicioMateriaPlan.actualizar(datosMateriaPlan)
        return ResponseEntity.ok("""
            Materia ${datosMateriaPlan.idMateria} actualizada correctamente en el plan de la carrera ${datosMateriaPlan.carrera}
        """.trimIndent().also { msg ->
            logger.trace("crear devuelve {}", msg)
        })
    }

    @Timed("materia.plan.ver.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerMateriaPlanSuUniversidad()")
    @GetMapping("/ver-mi-universidad")
    fun verMiUniversidad(): ResponseEntity<List<DatosMateriaPlan>> {
        logger.trace("entrado a endpoint ver-mi-universidad")
        return servicioMateriaPlan.verMiUniversidad().let {
            logger.trace("ver-mi-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("materia.plan.ver.por.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerMateriaPlan()")
    @GetMapping("/ver-por-universidad")
    fun verPorUniversidad(@RequestParam("universidad") universidad: String): ResponseEntity<List<DatosMateriaPlan>> {
        logger.trace("entrado a endpoint ver-por-universidad")
        return servicioMateriaPlan.verPorUniversidad(universidad).let {
            logger.trace("ver-por-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("materia.plan.actualizar.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeEditarMateriaPlanSuUniversidad(#datosMateriaPlan)")
    @PatchMapping("/actualizar-mi-universidad")
    fun actualizarMiUniversidad(@RequestBody datosMateriaPlan: DatosMateriaPlan): ResponseEntity<String> {
        logger.trace("entrado a endpoint actualizar-mi-universidad")
        servicioMateriaPlan.actualizar(datosMateriaPlan)
        return ResponseEntity.ok("Se actualizó la materia ${datosMateriaPlan.idMateria} correctamente.".also { msg ->
            logger.trace("actualizar-mi-universidad devuelve {}", msg)
        })
    }

    @Timed("materia.plan.actualizar")
    @PreAuthorize("@servicioAutorizacion.puedeEditarMateriaPlan()")
    @PatchMapping("/actualizar")
    fun actualizar(@RequestBody datosMateriaPlan: DatosMateriaPlan): ResponseEntity<String> {
        logger.trace("entrado a endpoint actualizar")
        servicioMateriaPlan.actualizar(datosMateriaPlan)
        return ResponseEntity.ok("Se actualizó la materia ${datosMateriaPlan.idMateria} correctamente.".also { msg ->
            logger.trace("actualizar devuelve {}", msg)
        })
    }

    @Timed("materia.plan.borrar.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarMateriaPlanSuUniversidad(#id)")
    @DeleteMapping("/borrar-mi-universidad")
    fun borrarMiUniversidad(@RequestParam("id") id: String): ResponseEntity<String> {
        logger.trace("entrado a endpoint borrar-mi-universidad")
        return servicioMateriaPlan.borrar(id).let {
            ResponseEntity.ok("Se borró la materia correctamente.".also { msg ->
                logger.trace("borrar-mi-universidad devuelve {}", msg)
            })
        }
    }

    @Timed("materia.plan.borrar")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarMateriaPlan()")
    @DeleteMapping("/borrar")
    fun borrar(@RequestParam("id") id: String): ResponseEntity<String> {
        logger.trace("entrado a endpoint borrar")
        return servicioMateriaPlan.borrar(id).let {
            ResponseEntity.ok("Se borró la materia correctamente.".also { msg ->
                logger.trace("borrar devuelve {}", msg)
            })
        }
    }

}
package io.github.yb.server.web

import io.github.yb.server.modelos.DatosMateria
import io.github.yb.server.modelos.NuevoMateria
import io.github.yb.server.servicios.ServicioAutorizacion
import io.github.yb.server.servicios.ServicioMateria
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/materia")
class ControladorMateria(val servicioMateria: ServicioMateria) {

    companion object {
        private val logger = LoggerFactory.getLogger(ControladorMateria::class.java)
    }

    @Timed("materia.crear.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeCrearMateriaSuUniversidad(#nuevoMateria)")
    @PostMapping("/crear-mi-universidad")
    fun crearMiUniversidad(@RequestBody nuevoMateria: NuevoMateria): ResponseEntity<String> {
        logger.trace("entrado a endpoint crear-mi-universidad")
        return servicioMateria.crear(nuevoMateria).let {
            ResponseEntity.ok("Se guardó la materia ${it.idMateria} correctamente.".also { msg ->
                logger.trace("crear-mi-universidad devuelve {}", msg)
            })
        }
    }

    @Timed("materia.crear")
    @PreAuthorize("@servicioAutorizacion.puedeCrearMateria()")
    @PostMapping("/crear")
    fun crear(@RequestBody nuevoMateria: NuevoMateria) {
        logger.trace("entrado a endpoint crear")
        return servicioMateria.crear(nuevoMateria).let {
            ResponseEntity.ok("Se guardó la materia ${it.idMateria} correctamente.".also { msg ->
                logger.trace("crear devuelve {}", msg)
            })
        }
    }

    @Timed("materia.ver.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeCrearMateria()")
    @GetMapping("/ver-mi-universidad")
    fun verMiUniversidad(): ResponseEntity<List<DatosMateria>> {
        logger.trace("entrado a endpoint ver-mi-universidad")
        return servicioMateria.verMiUniversidad().let {
            logger.trace("ver-mi-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("materia.ver.por.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerMaterias()")
    @GetMapping("/ver-por-universidad")
    fun verPorUniversidad(
        @RequestParam("universidad") universidad: String
    ): ResponseEntity<List<DatosMateria>> {
        logger.trace("entrado a endpoint ver-por-universidad")
        return servicioMateria.verPorUniversidad(universidad).let {
            logger.trace("ver-por-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("materia.actualizar.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeEditarMateriasSuUniversidad(#datosMateria)")
    @PatchMapping("/actualizar-mi-universidad")
    fun actualizarMiUniversidad(@RequestBody datosMateria: DatosMateria): ResponseEntity<String> {
        logger.trace("entrado a endpoint actualizar-mi-universidad")
        servicioMateria.actualizar(datosMateria)
        return ResponseEntity.ok("La materia ${datosMateria.idMateria} fue actualizada correctamente.".also { msg ->
            logger.trace("actualizar-mi-universidad devuelve {}", msg)
        })
    }

    @Timed("materia.actualizar")
    @PreAuthorize("@servicioAutorizacion.puedeEditarMaterias()")
    @PatchMapping("/actualiza")
    fun actualiza(@RequestBody datosMateria: DatosMateria): ResponseEntity<String> {
        logger.trace("entrado a endpoint actualiza")
        servicioMateria.actualizar(datosMateria)
        return ResponseEntity.ok("La materia ${datosMateria.idMateria} fue actualizada correctamente.".also { msg ->
            logger.trace("actualiza devuelve {}", msg)
        })
    }

    @Timed("materia.borrar.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarMateriasSuUniversidad(#id)")
    @DeleteMapping("/borrar-mi-universidad")
    fun borrarMiUniversidad(@RequestParam("id") id: String): ResponseEntity<String> {
        logger.trace("entrado a endpoint borrar-mi-universidad")
        servicioMateria.borrar(id)
        return ResponseEntity.ok("Se borró correctamente la materia con id $id.".also { msg ->
            logger.trace("borrar-mi-universidad devuelve {}", msg)
        })
    }

    @Timed("materia.borrar")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarMaterias()")
    @DeleteMapping("/borrar")
    fun borrar(@RequestParam("id") id: String): ResponseEntity<String> {
        logger.trace("entrado a endpoint borrar")
        servicioMateria.borrar(id)
        return ResponseEntity.ok("Se borró correctamente la materia con id $id.".also { msg ->
            logger.trace("borrar devuelve {}", msg)
        })
    }
}
package io.github.yb.server.web

import io.github.yb.server.modelos.DatosApunte
import io.github.yb.server.modelos.NuevoApunte
import io.github.yb.server.servicios.ServicioApunte
import io.github.yb.server.servicios.ServicioAutorizacion
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
@RequestMapping("/v1/apunte")
class ControladorApunte(
    val servicioApunte: ServicioApunte
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ControladorApunte::class.java)
    }

    @Timed("apunte.ver.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerApuntesSuUniversidad()")
    @GetMapping("/ver-mi-universidad")
    fun verMiUniversidad(): ResponseEntity<List<DatosApunte>> {
        logger.trace("entrado a endpoint ver-mi-universidad")
        return servicioApunte.verMiUniversidad().let{
            logger.trace("ver-mi-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("apunte.por.materia.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerApuntesSuUniversidad()")
    @GetMapping("/ver-por-materia-mi-universidad")
    fun verPorMateriaMiUniversidad(@RequestParam("id-materia") idMateria: String): ResponseEntity<List<DatosApunte>> {
        logger.trace("entrado a endpoint ver-por-materia-mi-universidad")
        return servicioApunte.verPorMateriaMiUniversidad(idMateria).let{
            logger.trace("ver-por-materia-mi-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("apunte.ver.por.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerApuntes()")
    @GetMapping("/ver-por-universidad")
    fun verPorUniversidad(@RequestParam("universidad") universidad: String): ResponseEntity<List<DatosApunte>> {
        logger.trace("entrado a endpoint ver-por-universidad")
        return servicioApunte.verPorUniversidad(universidad).let{
            logger.trace("ver-por-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("apunte.ver.por.materia.por.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerApuntes()")
    @GetMapping("/ver-por-materia-por-universidad")
    fun verPorMateriaPorUniversidad(
        @RequestParam("id-materia") idMateria: String,
        @RequestParam("universidad") universidad: String
    ): ResponseEntity<List<DatosApunte>> {
        logger.trace("entrado a endpoint ver-por-materia-por-universidad")
        return servicioApunte.verPorMateriaUniversidad(idMateria, universidad).let{
            logger.trace("ver-por-materia-por-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("apunte.crear.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeCrearApuntesSuUniversidad(#nuevoApunte)")
    @PostMapping("/crear-mi-universidad")
    fun crearMiUniversidad(@RequestBody nuevoApunte: NuevoApunte ): ResponseEntity<String> {
        logger.trace("entrado a endpoint crear-mi-universidad")
        return servicioApunte.crear(nuevoApunte).let {
            ResponseEntity.ok("Se creó el nuevo apunte con id ${it.id}.".also { msg ->
                logger.trace("crear-mi-universidad devuelve {}", msg)
            })
        }
    }

    @Timed("apunte.actualizar.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeActualizarApuntesSuUniversidad(#nuevoApunte)")
    @PatchMapping("/actualizar-mi-universidad")
    fun actualizarMiUniversidad(@RequestBody datosApunte: DatosApunte): ResponseEntity<String> {
        logger.trace("entrado a endpoint actualizar-mi-universidad")
        servicioApunte.actualizar(datosApunte)
        return ResponseEntity.ok("Se actualizo el apunte con id ${datosApunte.id} correctamente.".also { msg ->
            logger.trace("actualizar-mi-universidad devuelve {}", msg)
        })
    }

    @Timed("apunte.borrar.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarApuntesSuUniversidad(#nuevoApunte)")
    @DeleteMapping("/borrar-mi-universidad")
    fun borrarMiUniversidad(@RequestParam("id") id: String): ResponseEntity<String> {
        logger.trace("entrado a endpoint borrar-mi-universidad")
        servicioApunte.borrar(id)
        return ResponseEntity.ok("Se borró el apunte con id $id.".also { msg ->
            logger.trace("borrar-mi-universidad devuelve {}", msg)
        })
    }

    @Timed("apunte.crear")
    @PreAuthorize("@servicioAutorizacion.puedeCrearApuntes()")
    @PostMapping("/crear")
    fun crear(@RequestBody nuevoApunte: NuevoApunte ): ResponseEntity<String> {
        logger.trace("entrado a endpoint crear")
        return servicioApunte.crear(nuevoApunte).let {
            ResponseEntity.ok("Se creó el nuevo apunte con id ${it.id}.".also { msg ->
                logger.trace("crear devuelve {}", msg)
            })
        }
    }

    @Timed("apunte.actualizar")
    @PreAuthorize("@servicioAutorizacion.puedeActualizarApuntes()")
    @PatchMapping("/actualizar")
    fun actualizar(@RequestBody datosApunte: DatosApunte): ResponseEntity<String> {
        logger.trace("entrado a endpoint actualizar")
        servicioApunte.actualizar(datosApunte)
        return ResponseEntity.ok("Se actualizo el apunte con id ${datosApunte.id} correctamente.".also { msg ->
            logger.trace("actualizar devuelve {}", msg)
        })
    }

    @Timed("apunte.borrar")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarApuntes()")
    @DeleteMapping("/borrar")
    fun borrar(@RequestParam("id") id: String): ResponseEntity<String> {
        logger.trace("entrado a endpoint borrar")
        servicioApunte.borrar(id)
        return ResponseEntity.ok("Se borró el apunte con id $id.".also { msg ->
            logger.trace("borrar devuelve {}", msg)
        })
    }
}
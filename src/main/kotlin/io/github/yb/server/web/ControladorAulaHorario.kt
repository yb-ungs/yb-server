package io.github.yb.server.web

import io.github.yb.server.modelos.DatosAulaHorario
import io.github.yb.server.modelos.NuevoAulaHorario
import io.github.yb.server.servicios.ServicioAulaHorario
import io.github.yb.server.servicios.ServicioAutorizacion
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/aula-horario")
class ControladorAulaHorario(
    val servicioAulaHorario: ServicioAulaHorario
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ControladorAulaHorario::class.java)
    }

    @Timed("aula.horario.ver.por.comision.por.materia.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerAulaHorarioSuUniversidad()")
    @GetMapping("/ver-por-comision-por-materia-mi-universidad")
    fun verPorComisionPorMateriaMiUniversidad(
        @RequestParam("comision") comision: String,
        @RequestParam("materia") materia: String
    ): ResponseEntity<List<DatosAulaHorario>> {
        logger.trace("entrado a endpoint ver-por-comision-por-materia-mi-universidad")
        return servicioAulaHorario.verPorComisionPorMateriaMiUniversidad(comision, materia)
            .let {
                logger.trace("ver-por-comision-por-materia-mi-universidad devuelve {} filas", it.size)
                ResponseEntity.ok(it)
            }
    }

    @Timed("aula.horario.ver.por.comision.por.materia.por.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerAulaHorario()")
    @GetMapping("/ver-por-comision-por-materia-por-universidad")
    fun verPorComisionPorMateriaPorUniversidad(
        @RequestParam("comision") comision: String,
        @RequestParam("id-materia") idMateria: String,
        @RequestParam("universidad") universidad: String
    ): ResponseEntity<List<DatosAulaHorario>>  {
        logger.trace("entrado a endpoint ver-por-comision-por-materia-por-universidad")
        return servicioAulaHorario.verPorComisionPorMateriaPorUniversidad(comision, idMateria, universidad)
            .let {
                logger.trace("ver-por-comision-por-materia-por-universidad devuelve {} filas", it.size)
                ResponseEntity.ok(it)
            }
    }

    @Timed("aula.horario.crear.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeCrearAulaHorarioSuUniversidad(#nuevoAulaHorario)")
    @PostMapping("/crear-mi-universidad")
    fun crearMiUniversidad(
        @RequestBody nuevoAulaHorario: NuevoAulaHorario
    ): ResponseEntity<String>   {
        logger.trace("entrado a endpoint crear-mi-universidad")
        return servicioAulaHorario.crear(nuevoAulaHorario)
            .let {
                ResponseEntity.ok("AulaHorario con id ${it.id} creado correctamente.".also { msg ->
                    logger.trace("crear-mi-universidad devuelve {}", msg)
                })
            }
    }

    @Timed("aula.horario.actualizar.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeActualizarAulaHorarioSuUniversidad(#datosAulaHorario)")
    @PatchMapping("/actualizar-mi-universidad")
    fun actualizarMiUniversidad(
        @RequestBody datosAulaHorario: DatosAulaHorario
    ): ResponseEntity<String>  {
        logger.trace("entrado a endpoint actualizar-mi-universidad")
        servicioAulaHorario.actualizar(datosAulaHorario)
        return ResponseEntity.ok("AulaHorario con id ${datosAulaHorario.id} actualizado correctamente.".also { msg ->
            logger.trace("actualizar-mi-universidad devuelve {}", msg)
        })
    }

    @Timed("aula.horario.borrar.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarAulaHorarioSuUniversidad(#id)")
    @DeleteMapping("/borrar-mi-universidad")
    fun borrarMiUniversidad(
        @RequestParam("id") id: String
    ): ResponseEntity<String>  {
        logger.trace("entrado a endpoint borrar-mi-universidad")
        servicioAulaHorario.borrar(id)
        return ResponseEntity.ok("AulaHorario con id $id borrado correctamente.".also { msg ->
            logger.trace("borrar-mi-universidad devuelve {}", msg)
        })
    }

    @Timed("aula.horario.crear")
    @PreAuthorize("@servicioAutorizacion.puedeCrearAulaHorario()")
    @PostMapping("/crear")
    fun crear(
        @RequestBody nuevoAulaHorario: NuevoAulaHorario
    ): ResponseEntity<String>  {
        logger.trace("entrado a endpoint crear")
        return servicioAulaHorario.crear(nuevoAulaHorario)
            .let{
                ResponseEntity.ok("AulaHorario con id ${it.id} creado correctamente.".also { msg ->
                    logger.trace("crear devuelve {}", msg)
                })
            }
    }

    @Timed("aula.horario.actualizar")
    @PreAuthorize("@servicioAutorizacion.puedeEditarAulaHorario()")
    @PatchMapping("/actualizar")
    fun actualizar(
        @RequestBody datosAulaHorario: DatosAulaHorario
    ): ResponseEntity<String>  {
        logger.trace("entrado a endpoint actualizar")
        servicioAulaHorario.actualizar(datosAulaHorario)
        return ResponseEntity.ok("AulaHorario con id ${datosAulaHorario.id} actualizado correctamente.".also { msg ->
            logger.trace("actualizar devuelve {}", msg)
        })
    }

    @Timed("aula.horario.borrar")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarAulaHorario()")
    @DeleteMapping("/borrar")
    fun borrar(
        @RequestParam("id") id: String
    ): ResponseEntity<String>  {
        logger.trace("entrado a endpoint borrar")
        servicioAulaHorario.borrar(id)
        return ResponseEntity.ok("AulaHorario con id $id borrado correctamente.".also { msg ->
            logger.trace("borrar devuelve {}", msg)
        })
    }
}
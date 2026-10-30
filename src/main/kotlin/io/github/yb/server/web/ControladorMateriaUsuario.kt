package io.github.yb.server.web

import io.github.yb.server.modelos.DatosMateriaUsuario
import io.github.yb.server.modelos.NuevoMateriaUsuario
import io.github.yb.server.servicios.ServicioAutorizacion
import io.github.yb.server.servicios.ServicioMateriaUsuario
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/materia-usuario")
class ControladorMateriaUsuario(
    val servicioMateriaUsuario: ServicioMateriaUsuario
) {

    companion object {
        private val logger = LoggerFactory.getLogger(ControladorMateriaUsuario::class.java)
    }

    @Timed("materia.usuario.crear.propio")
    @PreAuthorize("@servicioAutorizacion.puedeCrearMateriaUsuarioPropio(#nuevoMateriaUsuario)")
    @PostMapping("/crear-propio")
    fun crearPropio(@RequestBody nuevoMateriaUsuario: NuevoMateriaUsuario): ResponseEntity<String> {
        logger.trace("entrado a endpoint crear-propio")
        return servicioMateriaUsuario.crear(nuevoMateriaUsuario).let {
            ResponseEntity.ok("Se agregó la materia ${it.idMateria} correctamente.".also { msg ->
                logger.trace("crear-propio devuelve {}", msg)
            })
        }
    }

    @Timed("materia.usuario.ver.propio")
    @PreAuthorize("@servicioAutorizacion.puedeVerMateriaUsuarioPropio()")
    @GetMapping("/ver-propio")
    fun verPropio(): ResponseEntity<List<DatosMateriaUsuario>> {
        logger.trace("entrado a endpoint ver-propio")
        return servicioMateriaUsuario.verPropias().let {
            logger.trace("ver-propio devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("materia.usuario.ver.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerMateriaUsuarioSuUniversidad()")
    @GetMapping("/ver-mi-universidad")
    fun verUniversidad(): ResponseEntity<List<DatosMateriaUsuario>> {
        logger.trace("entrado a endpoint ver-mi-universidad")
        return servicioMateriaUsuario.verPropiaUniversidad().let {
            logger.trace("ver-mi-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("materia.usuario.ver.por.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerMateriaUsuarioPorUniversidad()")
    @GetMapping("/ver-por-universidad")
    fun verPorUniversidad(
        @RequestParam("universidad") universidad: String
    ): ResponseEntity<List<DatosMateriaUsuario>> {
        logger.trace("entrado a endpoint ver-por-universidad")
        return servicioMateriaUsuario.verPorUniversidad(universidad).let {
            logger.trace("ver-por-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("materia.usuario.borrar.propio")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarMateriaUsuarioPropio()")
    @DeleteMapping("/borrar-propio")
    fun borrarPropio(@RequestParam("id") id: String): ResponseEntity<String> {
        logger.trace("entrado a endpoint borrar-propio")
        servicioMateriaUsuario.borrar(id)
        return ResponseEntity.ok("Se ha borrado la materia con exito.".also { msg ->
            logger.trace("borrar-propio devuelve {}", msg)
        })
    }

    @Timed("materia.usuario.crear")
    @PreAuthorize("@servicioAutorizacion.puedeCrearMateriaUsuario()")
    @PostMapping("/crear")
    fun crear(@RequestBody nuevoMateriaUsuario: NuevoMateriaUsuario): ResponseEntity<String> {
        logger.trace("entrado a endpoint crear")
        return servicioMateriaUsuario.crear(nuevoMateriaUsuario).let {
            ResponseEntity.ok("Se ha agregado la materia ${it.idMateria} correctamente.".also { msg ->
                logger.trace("crear devuelve {}", msg)
            })
        }
    }

    @Timed("materia.usuario.borrar")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarMateriaUsuario()")
    @PostMapping("/borrar")
    fun borrar(@RequestParam("id") id: String): ResponseEntity<String> {
        logger.trace("entrado a endpoint borrar")
        servicioMateriaUsuario.borrar(id)
        return ResponseEntity.ok("Se ha borrado la materia con exito.".also { msg ->
            logger.trace("borrar devuelve {}", msg)
        })
    }
}
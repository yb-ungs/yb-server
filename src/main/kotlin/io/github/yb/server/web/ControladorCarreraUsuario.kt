package io.github.yb.server.web

import io.github.yb.server.modelos.DatosCarreraUsuario
import io.github.yb.server.modelos.NuevoCarreraUsuario
import io.github.yb.server.servicios.ServicioAutorizacion
import io.github.yb.server.servicios.ServicioCarreraUsuario
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/carrera")
@RestController
class ControladorCarreraUsuario(
    val servicioCarreraUsuario: ServicioCarreraUsuario
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ControladorCarreraUsuario::class.java)
    }

    @Timed("aula.horario.ver.propias")
    @PreAuthorize("@servicioAutorizacion.puedeVerSusCarreras()")
    @GetMapping("/ver-propias")
    fun verPropias(): ResponseEntity<List<DatosCarreraUsuario>> {
        logger.trace("entrado a endpoint ver-propias")
        return servicioCarreraUsuario.verMisCarreras().let {
            logger.trace("ver-propias devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("aula.horario.ver.por.usuario.mi.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerCarrerasSuUniversidad(#usuario)")
    @GetMapping("/ver-por-usuario-mi-universidad")
    fun verPorUsuarioMiUniversidad(
        @RequestParam("usuario") usuario: String
    ): ResponseEntity<List<DatosCarreraUsuario>> {
        logger.trace("entrado a endpoint ver-por-usuario-mi-universidad")
        return servicioCarreraUsuario.verPorUsuario(usuario).let {
            logger.trace("ver-por-usuario-mi-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("aula.horario.ver.por.usuario")
    @PreAuthorize("@servicioAutorizacion.puedeVerCarreras()")
    @GetMapping("/ver-por-usuario")
    fun verPorUsuario(
        @RequestParam("usuario") usuario: String
    ): ResponseEntity<List<DatosCarreraUsuario>> {
        logger.trace("entrado a endpoint ver-por-usuario")
        return servicioCarreraUsuario.verPorUsuario(usuario).let {
            logger.trace("ver-por-usuario devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }

    @Timed("aula.horario.crear.propia")
    @PreAuthorize("@servicioAutorizacion.puedeCrearCarreraPropia()")
    @PostMapping("/crear-propia")
    fun crearPropia(@RequestBody nuevoCarreraUsuario: NuevoCarreraUsuario): ResponseEntity<String> {
        logger.trace("entrado a endpoint crear-propia")
        return servicioCarreraUsuario.crear(nuevoCarreraUsuario).let { ResponseEntity.ok(
            "Se agregó la carrera ${it.carrera} a tus carreras.".also { msg ->
                logger.trace("crear-propia devuelve {}", msg)
            }
        ) }
    }

    @Timed("aula.horario.crear")
    @PreAuthorize("@servicioAutorizacion.puedeCrearCarrera()")
    @PostMapping("/crear")
    fun crear(@RequestBody nuevoCarreraUsuario: NuevoCarreraUsuario) {
        logger.trace("entrado a endpoint crear")
        return servicioCarreraUsuario.crear(nuevoCarreraUsuario).let { ResponseEntity.ok(
            "Se agregó la carrera ${it.carrera} a las carreras del usuario ${it.username}.".also { msg ->
                logger.trace("crear devuelve {}", msg)
            }
        ) }
    }

    @Timed("aula.horario.borrar.propia")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarCarreraPropia()")
    @PostMapping("/borrar-propia")
    fun borrarPropia(@RequestParam("id") id: String): ResponseEntity<String> {
        logger.trace("entrado a endpoint borrar-propia")
        servicioCarreraUsuario.borrar(id)
        return ResponseEntity.ok("Se borró la carrera correctamente.".also { msg ->
            logger.trace("borrar-propia devuelve {}", msg)
        })
    }

    @Timed("aula.horario.borrar")
    @PreAuthorize("@servicioAutorizacion.puedeBorrar()")
    @PostMapping("/borrar")
    fun borrar(@RequestParam("id") id: String): ResponseEntity<String> {
        logger.trace("entrado a endpoint borrar")
        servicioCarreraUsuario.borrar(id)
        return ResponseEntity.ok("Se borró la carrera correctamente.".also { msg ->
            logger.trace("borrar devuelve {}", msg)
        })
    }
}
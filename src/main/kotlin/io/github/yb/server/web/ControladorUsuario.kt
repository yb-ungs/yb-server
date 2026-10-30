package io.github.yb.server.web

import io.github.yb.server.modelos.CambioPass
import io.github.yb.server.modelos.CambioPerfil
import io.github.yb.server.modelos.DatosUsuario
import io.github.yb.server.modelos.NuevoUsuario
import io.github.yb.server.servicios.ServicioAutorizacion
import io.github.yb.server.servicios.ServicioUsuario
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/usuario")
class ControladorUsuario(
    val servicioUsuario: ServicioUsuario,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ControladorUsuario::class.java)
    }

    @Timed("usuario.agregar")
    @PreAuthorize("@servicioAutorizacion.puedeAgregarUsuario()")
    @PostMapping("/agregar")
    fun agregar(@RequestBody nuevoUsuario: NuevoUsuario): ResponseEntity<String> {
        logger.trace("entrado a endpoint agregar")
        if (servicioUsuario.usuarioExiste(nuevoUsuario.username)) {
            logger.trace("agregar: usuario ya existe")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        servicioUsuario.crear(nuevoUsuario)
        return ResponseEntity.ok("Tu usuario se ha creado correctamente!".also { msg ->
            logger.trace("agregar devuelve {}", msg)
        })
    }

    // solo admins
    @Timed("usuario.borrar")
    @PreAuthorize("@servicioAutorizacion.puedeBorrarUsuario()")
    @DeleteMapping("/borrar")
    fun borrar(@RequestParam("username") username: String): ResponseEntity<String> {
        logger.trace("entrado a endpoint borrar")
        servicioUsuario.borrar(username)
        return ResponseEntity.ok("Usuario $username borrado".also { msg ->
            logger.trace("borrar devuelve {}", msg)
        })
    }

    @Timed("usuario.actualizar.datos.propios")
    @PreAuthorize("@servicioAutorizacion.puedeActualizarDatosPropios(#datos.username)")
    @PatchMapping("/actualizar-datos-propios")
    fun actualizarDatosPropios(@RequestBody datos: DatosUsuario): ResponseEntity<String> {
        logger.trace("entrado a endpoint actualizar-datos-propios")
        servicioUsuario.actualizar(datos)
        return ResponseEntity.ok("Actualizado!".also { msg ->
            logger.trace("actualizar-datos-propios devuelve {}", msg)
        })
    }

    @Timed("usuario.cambiar.pass.propia")
    @PreAuthorize("@servicioAutorizacion.puedeCambiarPassPropia(#cambioPass.username)")
    @PatchMapping("/cambiar-pass-propia")
    fun cambiarPassPropia(@RequestBody cambioPass: CambioPass): ResponseEntity<String> {
        logger.trace("entrado a endpoint cambiar-pass-propia")
        servicioUsuario.cambiarPass(cambioPass)
        return ResponseEntity.ok("Contraseña cambiada!".also { msg ->
            logger.trace("cambiar-pass-propia devuelve {}", msg)
        })
    }

    @Timed("usuario.ver.datos.propios")
    @PreAuthorize("@servicioAutorizacion.puedeVerDatosPropios()")
    @GetMapping("/ver-datos-propios")
    fun verDatosPropios(): ResponseEntity<DatosUsuario> {
        logger.trace("entrado a endpoint ver-datos-propios")
        return ResponseEntity.ok(servicioUsuario.datosPropios().also {
            logger.trace("ver-datos-propios devuelve usuario {}", it.toString())
        })
    }

    @Timed("usuario.cambiar.perfil")
    @PreAuthorize("@servicioAutorizacion.puedeCambiarPerfil()")
    @PatchMapping("/cambiar-perfil")
    fun cambiarPerfil(@RequestBody cambioPerfil: CambioPerfil): ResponseEntity<String> {
        logger.trace("entrado a endpoint cambiar-perfil")
        servicioUsuario.cambiarPeril(cambioPerfil)
        return ResponseEntity.ok("Perfil de usuario ${cambioPerfil.username} actualizado a ${cambioPerfil.perfil}".also { msg ->
            logger.trace("cambiar-perfil devuelve {}", msg)
        })
    }


    @Timed("usuario.encontrar.por.nombre")
    @PreAuthorize("@servicioAutorizacion.puedeVerDatosUsuario()")
    @GetMapping("/encontrar-por-nombre")
    fun encontrarPorNombre(@RequestParam("username") username: String): ResponseEntity<DatosUsuario> {
        logger.trace("entrado a endpoint encontrar-por-nombre")
        return servicioUsuario.encontrarPorNombre(username).let {
            logger.trace("encontrar-por-nombre devuelve usuario {}", it.toString())
            ResponseEntity.ok(it)
        }
    }

    @Timed("usuario.encontrar.por.universidad")
    @PreAuthorize("@servicioAutorizacion.puedeVerUsuariosPorUniversidad(#universidad)")
    @GetMapping("/encontrar-por-universidad")
    fun encontrarPorUniversidad(@RequestParam("universidad") universidad: String): ResponseEntity<List<DatosUsuario>> {
        logger.trace("entrado a endpoint encontrar-por-universidad")
        return servicioUsuario.encontrarPorUniversidad(universidad).let {
            logger.trace("encontrar-por-universidad devuelve {} filas", it.size)
            ResponseEntity.ok(it)
        }
    }
}
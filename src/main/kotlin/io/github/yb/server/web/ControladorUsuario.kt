package io.github.yb.server.web

import io.github.yb.server.modelos.CambioPass
import io.github.yb.server.modelos.CambioPerfil
import io.github.yb.server.modelos.DatosUsuario
import io.github.yb.server.modelos.NuevoUsuario
import io.github.yb.server.servicios.ServicioUsuario
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/usuario")
class ControladorUsuario(
    val servicioUsuario: ServicioUsuario,
) {

    @PreAuthorize("@servicioAutorizacion.puedeAgregarUsuario()")
    @PostMapping("/agregar")
    fun agregar(@RequestBody nuevoUsuario: NuevoUsuario): ResponseEntity<String> {
        if(servicioUsuario.usuarioExiste(nuevoUsuario.username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        servicioUsuario.guardarNuevoUsuario(nuevoUsuario)
        return ResponseEntity.ok("Tu usuario se ha creado correctamente!")
    }

    // solo admins
    @PreAuthorize("@servicioAutorizacion.puedeBorrarUsuario()")
    @DeleteMapping("/borrar")
    fun borrar(@RequestParam("username") username: String): ResponseEntity<String> {
        servicioUsuario.borrarUsuario(username)
        return ResponseEntity.ok("Usuario $username borrado")
    }

    @PreAuthorize("@servicioAutorizacion.puedeActualizarDatosPropios(#datos.username)")
    @PatchMapping("/actualizar-datos-propios")
    fun actualizarDatosPropios(@RequestBody datos: DatosUsuario): ResponseEntity<String> {
        servicioUsuario.actualizarUsuario(datos)
        return ResponseEntity.ok("Actualizado!")
    }

    @PreAuthorize("@servicioAutorizacion.puedeCambiarPassPropia(#cambioPass.username)")
    @PatchMapping("/cambiar-pass-propia")
    fun cambiarPassPropia(@RequestBody cambioPass: CambioPass): ResponseEntity<String> {
        servicioUsuario.cambiarPass(cambioPass)
        return ResponseEntity.ok("Contraseña cambiada!")
    }

    @PreAuthorize("@servicioAutorizacion.puedeVerDatosPropios()")
    @GetMapping("/ver-datos-propios")
    fun verDatosPropios(): ResponseEntity<DatosUsuario> {
        return ResponseEntity.ok(servicioUsuario.datosPropios())
    }

    @PreAuthorize("@servicioAutorizacion.puedeCambiarPerfil()")
    @PatchMapping("/cambiar-perfil")
    fun cambiarPerfil(@RequestBody cambioPerfil: CambioPerfil): ResponseEntity<String> {
        servicioUsuario.cambiarPeril(cambioPerfil)
        return ResponseEntity.ok("Perfil de usuario ${cambioPerfil.username} actualizado a ${cambioPerfil.perfil}")
    }


    @PreAuthorize("@servicioAutorizacion.puedeVerDatosUsuario()")
    @GetMapping("/encontrar-por-nombre")
    fun encontrarPorNombre(username: String): ResponseEntity<DatosUsuario> {
        val datos = servicioUsuario.encontrarPorNombre(username)
        return ResponseEntity.ok(datos)
    }

    @PreAuthorize("@servicioAutorizacion.puedeVerUsuariosPorUniversidad(#universidad)")
    @GetMapping("/encontrar-por-universidad")
    fun encontrarPorUniversidad(universidad: String): ResponseEntity<List<DatosUsuario>> {
        return ResponseEntity.ok(servicioUsuario.encontrarPorUniversidad(universidad))
    }

}
package io.github.yb.server.servicios

import io.github.yb.server.modelos.*
import io.github.yb.server.repositorios.*
import io.github.yb.server.util.Permiso
import io.github.yb.server.util.Permiso.*
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class ServicioAutorizacion(
    val servicioContextoSeguridad: ServicioContextoSeguridad,
    val repositorioUsuario: RepositorioUsuario,
    val repositorioAnuncio: RepositorioAnuncio,
    val repositorioAulaHorario: RepositorioAulaHorario,
    val repositorioMateria: RepositorioMateria,
    val repositorioMateriaPlan: RepositorioMateriaPlan
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ServicioAutorizacion::class.java)
    }
    private fun tieneAutoridad(autoridad: Permiso): Boolean {
        logger.debug("Comprobando autoridad ${autoridad.name} para usuario ${servicioContextoSeguridad.usuario}")
        return servicioContextoSeguridad.tieneAutoridad(autoridad.name)
    }

    fun puedeAgregarUsuario(): Boolean {
        return tieneAutoridad(AGREGAR_USUARIO)
    }

    fun puedeBorrarUsuario(): Boolean {
        return tieneAutoridad(BORRAR_USUARIO)
    }

    fun puedeActualizarDatosPropios(username: String): Boolean {
        val detallesDeUsuario = servicioContextoSeguridad.detallesUsuario
        return detallesDeUsuario.username == username
                && tieneAutoridad(ACTUALIZAR_DATOS_PROPIOS)
    }

    fun puedeCambiarPassPropia(username: String): Boolean {
        val detallesDeUsuario = servicioContextoSeguridad.detallesUsuario
        return detallesDeUsuario.username == username || tieneAutoridad(CAMBIAR_PASS_PROPIA)
    }

    fun puedeCambiarPerfil(): Boolean {
        return tieneAutoridad(CAMBIAR_PERFIL)
    }

    fun puedeVerDatosPropios(): Boolean {
        return tieneAutoridad(VER_DATOS_PROPIOS)
    }

    fun puedeVerDatosUsuario(): Boolean {
        return tieneAutoridad(VER_DATOS_USUARIO)
    }

    fun puedeVerUsuariosPorUniversidad(universidad: String): Boolean {
        return tieneAutoridad(VER_USUARIOS_UNIVERSIDAD) && servicioContextoSeguridad.datosUsuario.universidad == universidad
    }

    fun puedeVerAnunciosSuUniversidad(): Boolean {
        return tieneAutoridad(VER_ANUNCIOS_UNIVERSIDAD)
    }

    fun puedeEditarAnunciosSuUniversidad(datosAnuncio: DatosAnuncio): Boolean {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(EDITAR_ANUNCIOS_UNIVERSIDAD) && datosUsuario.universidad == datosAnuncio.universidad
    }

    fun puedeBorrarAnunciosSuUniversidad(id: String): Boolean {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        val anuncio = repositorioAnuncio.findById(id).orElse(null)!!
        return tieneAutoridad(BORRAR_ANUNCIOS_UNIVERSIDAD) && datosUsuario.universidad == anuncio.universidad
    }

    fun puedeBorrarAnuncio(): Boolean {
        return tieneAutoridad(BORRAR_ANUNCIO)
    }

    fun puedeCrearAnuncio(): Boolean {
        return tieneAutoridad(CREAR_ANUNCIO)
    }

    fun puedeModificarAnuncio(): Boolean {
        return tieneAutoridad(MODIFICAR_ANUNCIO)
    }

    fun puedeEncontrarAnunciosPorCursor(): Boolean {
        return tieneAutoridad(ENCONTRAR_ANUNCIO)
    }

    fun puedeVerRegistros(): Boolean {
        return tieneAutoridad(VER_REGISTROS)
    }

    fun puedeVerApuntesSuUniversidad(): Boolean {
        return tieneAutoridad(VER_APUNTES_UNIVERSIDAD)
    }

    fun puedeCrearApuntesSuUniversidad(datosApunte: DatosApunte): Boolean {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(CREAR_APUNTES_UNIVERSIDAD) && datosUsuario.universidad == datosApunte.universidad
    }

    fun puedeActualizarApuntesSuUniversidad(datosApunte: DatosApunte): Boolean {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(EDITAR_APUNTES_UNIVERSIDAD) && datosUsuario.universidad == datosApunte.universidad
    }

    fun puedeBorrarApuntesSuUniversidad(datosApunte: DatosApunte): Boolean {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(BORRAR_APUNTES_UNIVERSIDAD) && datosUsuario.universidad == datosApunte.universidad
    }

    fun puedeVerApuntes(): Boolean {
        return tieneAutoridad(VER_APUNTES)
    }

    fun puedeCrearApuntes(): Boolean {
        return tieneAutoridad(CREAR_APUNTES)
    }

    fun puedeActualizarApuntes(): Boolean {
        return tieneAutoridad(EDITAR_APUNTES)
    }

    fun puedeBorrarApuntes(): Boolean {
        return tieneAutoridad(BORRAR_APUNTES)
    }

    fun puedeVerAulaHorarioSuUniversidad(): Boolean {
        return tieneAutoridad(VER_AULA_HORARIO_UNIVERSIDAD)
    }

    fun puedeVerAulaHorario(): Boolean {
        return tieneAutoridad(VER_AULA_HORARIO)
    }

    fun puedeCrearAulaHorarioSuUniversidad(nuevoAulaHorario: NuevoAulaHorario): Boolean {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(CREAR_AULA_HORARIO_UNIVERSIDAD) && datosUsuario.universidad == nuevoAulaHorario.universidad
    }

    fun puedeActualizarAulaHorarioSuUniversidad(datosAulaHorario: DatosAulaHorario): Boolean {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(EDITAR_AULA_HORARIO_UNIVERSIDAD) && datosUsuario.universidad == datosAulaHorario.universidad
    }

    fun puedeBorrarAulaHorarioSuUniversidad(id: String): Boolean {
        val aulaHorario = repositorioAulaHorario.findById(id).orElseThrow()
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(BORRAR_AULA_HORARIO_UNIVERSIDAD) && datosUsuario.universidad == aulaHorario.universidad
    }

    fun puedeCrearAulaHorario(): Boolean {
        return tieneAutoridad(CREAR_AULA_HORARIO)
    }

    fun puedeEditarAulaHorario(): Boolean {
        return tieneAutoridad(EDITAR_AULA_HORARIO)
    }

    fun puedeBorrarAulaHorario(): Boolean {
        return tieneAutoridad(BORRAR_AULA_HORARIO)
    }

    fun puedeVerSusCarreras(): Boolean {
        return tieneAutoridad(VER_CARRERAS_PROPIAS)
    }

    fun puedeVerCarrerasSuUniversidad(usuario: String): Boolean {
        val datosUsuarioSolicitante = servicioContextoSeguridad.datosUsuario
        val datosUsuarioBuscado = repositorioUsuario.encontrarPorUsuario(usuario)
            ?: throw UsernameNotFoundException("Usuario $usuario no encontrado")
        return datosUsuarioSolicitante.universidad == datosUsuarioBuscado.universidad
                && tieneAutoridad(VER_CARRERAS_UNIVERSIDAD)
    }

    fun puedeVerCarreras(): Boolean {
        return tieneAutoridad(VER_CARRERAS)
    }

    fun puedeCrearCarreraPropia(): Boolean {
        return tieneAutoridad(CREAR_CARRERA_PROPIA)
    }

    fun puedeCrearCarrera(): Boolean {
        return tieneAutoridad(CREAR_CARRERA)
    }

    fun puedeBorrarCarreraPropia(): Boolean {
        return tieneAutoridad(BORRAR_CARRERA_PROPIA)
    }

    fun puedeCrearMateriaSuUniversidad(nuevoMateria: NuevoMateria): Boolean {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(CREAR_MATERIA_UNIVERSIDAD) && datosUsuario.universidad == nuevoMateria.universidad
    }

    fun puedeCrearMateria(): Boolean {
        return tieneAutoridad(CREAR_MATERIA)
    }

    fun puedeVerMaterias(): Boolean {
        return tieneAutoridad(VER_MATERIA)
    }

    fun puedeEditarMateriasSuUniversidad(datosMateria: DatosMateria): Boolean {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(EDITAR_MATERIA_UNIVERSIDAD) && datosUsuario.universidad == datosMateria.universidad
    }

    fun puedeEditarMaterias(): Boolean {
        return tieneAutoridad(EDITAR_MATERIA)
    }

    fun puedeBorrarMateriasSuUniversidad(id: String): Boolean {
        val entidad = repositorioMateria.findById(id).orElseThrow()
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(EDITAR_MATERIA_UNIVERSIDAD) && entidad.universidad == datosUsuario.universidad
    }

    fun puedeBorrarMaterias(): Boolean {
        return tieneAutoridad(BORRAR_MATERIA)
    }

    fun puedeCrearMateriaPlanSuUniversidad(datosMateriaPlan: DatosMateriaPlan): Boolean {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(CREAR_MATERIA_PLAN_UNIVERSIDAD) && datosUsuario.universidad == datosMateriaPlan.universidad
    }

    fun puedeVerMateriaPlanSuUniversidad(): Boolean {
        return tieneAutoridad(VER_MATERIA_PLAN_UNIVERSIDAD)
    }

    fun puedeVerMateriaPlan(): Boolean {
        return tieneAutoridad(VER_MATERIA_PLAN)
    }

    fun puedeEditarMateriaPlanSuUniversidad(datosMateriaPlan: DatosMateriaPlan): Boolean {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(EDITAR_MATERIA_PLAN_UNIVERSIDAD) && datosUsuario.universidad == datosMateriaPlan.universidad
    }

    fun puedeEditarMateriaPlan(): Boolean {
        return tieneAutoridad(EDITAR_MATERIA_PLAN)
    }

    fun puedeBorrarMateriaPlanSuUniversidad(id: String): Boolean {
        val entidad = repositorioMateriaPlan.findById(id).orElseThrow()
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(BORRAR_MATERIA_PLAN_UNIVERSIDAD) && datosUsuario.universidad == entidad.universidad
    }

    fun puedeBorrarMateriaPlan(): Boolean {
        return tieneAutoridad(BORRAR_MATERIA_PLAN)
    }

    fun puedeCrearMateriaUsuarioPropio(nuevoMateriaUsuario: NuevoMateriaUsuario): Boolean {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return tieneAutoridad(VER_MATERIA_USUARIO_PROPIO)
                && datosUsuario.username == nuevoMateriaUsuario.idUsuario
                && datosUsuario.universidad == nuevoMateriaUsuario.idUsuario
                && repositorioMateriaPlan.verPorMateria(nuevoMateriaUsuario.idMateria).any {
            datosUsuario.universidad == it.universidad
        }
    }

    fun puedeVerMateriaUsuarioPropio(): Boolean {
        return tieneAutoridad(VER_MATERIA_USUARIO_PROPIO)
    }

    fun puedeVerMateriaUsuarioSuUniversidad(): Boolean {
        return tieneAutoridad(VER_MATERIA_USUARIO_UNIVERSIDAD)
    }

    fun puedeCrearMateriaUsuario(): Boolean {
        return tieneAutoridad(CREAR_MATERIA_USUARIO)
    }

    fun puedeBorrarMateriaUsuario(): Boolean {
        return tieneAutoridad(BORRAR_MATERIA_USUARIO)
    }
}
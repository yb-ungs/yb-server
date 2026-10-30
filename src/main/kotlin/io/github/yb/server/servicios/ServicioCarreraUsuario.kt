package io.github.yb.server.servicios

import com.github.f4b6a3.uuid.UuidCreator
import io.github.yb.server.entidades.CarreraUsuario
import io.github.yb.server.modelos.DatosCarreraUsuario
import io.github.yb.server.modelos.NuevoCarreraUsuario
import io.github.yb.server.repositorios.RepositorioCarreraUsuario
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class ServicioCarreraUsuario(
    val repositorioCarreraUsuario: RepositorioCarreraUsuario,
    val servicioContextoSeguridad: ServicioContextoSeguridad
) {

    fun NuevoCarreraUsuario.entidad(): CarreraUsuario {
        return CarreraUsuario(
            UuidCreator.getTimeOrderedEpoch().toString(),
            carrera,
            username,
            OffsetDateTime.now()
        )
    }

    fun CarreraUsuario.modelo(): DatosCarreraUsuario {
        return DatosCarreraUsuario(
            id,
            carrera,
            username,
            creadoEl.toString()
        )
    }

    fun DatosCarreraUsuario.entidad(): CarreraUsuario {
        return CarreraUsuario(
            id,
            carrera,
            username,
            OffsetDateTime.parse(creadoEl)
        )
    }

    fun verMisCarreras(): List<DatosCarreraUsuario> {
        val usuario = servicioContextoSeguridad.usuario
        return repositorioCarreraUsuario.verPorUsuario(usuario).map { it.modelo() }
    }

    fun verPorUsuario(usuario: String): List<DatosCarreraUsuario> {
        return repositorioCarreraUsuario.verPorUsuario(usuario).map { it.modelo() }
    }

    fun crear(nuevoCarreraUsuario: NuevoCarreraUsuario): CarreraUsuario {
        return repositorioCarreraUsuario.crear(nuevoCarreraUsuario.entidad())
    }

    fun borrar(id: String) {
        repositorioCarreraUsuario.deleteById(id)
    }
}
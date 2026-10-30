package io.github.yb.server.servicios

import com.github.f4b6a3.uuid.UuidCreator
import io.github.yb.server.entidades.MateriaUsuario
import io.github.yb.server.modelos.DatosMateriaUsuario
import io.github.yb.server.modelos.NuevoMateriaUsuario
import io.github.yb.server.repositorios.RepositorioMateriaUsuario
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class ServicioMateriaUsuario(
    val repositorioMateriaUsuario: RepositorioMateriaUsuario,
    val servicioContextoSeguridad: ServicioContextoSeguridad
) {

    fun NuevoMateriaUsuario.entidad(): MateriaUsuario{
        return MateriaUsuario(
            UuidCreator.getTimeOrderedEpoch().toString(),
            idMateria,
            idUsuario,
            OffsetDateTime.now()
        )
    }

    fun DatosMateriaUsuario.entidad(): MateriaUsuario {
        return MateriaUsuario(
            id,
            idMateria,
            idUsuario,
            OffsetDateTime.parse(creadoEl)
        )
    }

    fun MateriaUsuario.modelo(): DatosMateriaUsuario {
        return DatosMateriaUsuario(
            id,
            idMateria,
            idUsuario,
            creadoEl.toString()
        )
    }

    fun crear(nuevoMateriaUsuario: NuevoMateriaUsuario): MateriaUsuario {
        return repositorioMateriaUsuario.crear(nuevoMateriaUsuario.entidad())
    }

    fun verPropias(): List<DatosMateriaUsuario>{
        return repositorioMateriaUsuario.verPorUsuario(servicioContextoSeguridad.usuario).map { it.modelo() }
    }

    fun verPorUniversidad(universidad: String): List<DatosMateriaUsuario>{
        return repositorioMateriaUsuario.verPorUniversidad(universidad).map { it.modelo() }
    }

    fun borrar(id: String) {
        repositorioMateriaUsuario.deleteById(id)
    }

    fun verPropiaUniversidad(): List<DatosMateriaUsuario>{
        val universidad = servicioContextoSeguridad.datosUsuario.universidad
        return verPorUniversidad(universidad)
    }
}
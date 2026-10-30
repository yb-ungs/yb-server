package io.github.yb.server.servicios

import com.github.f4b6a3.uuid.UuidCreator
import io.github.yb.server.entidades.Materia
import io.github.yb.server.excepciones.ExcepcionElementoRepetido
import io.github.yb.server.modelos.DatosMateria
import io.github.yb.server.modelos.NuevoMateria
import io.github.yb.server.repositorios.RepositorioMateria
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service


@Service
class ServicioMateria(
    val repositorioMateria: RepositorioMateria,
    val servicioContextoSeguridad: ServicioContextoSeguridad
) {

    fun NuevoMateria.entidad(): Materia {
        return Materia(
            UuidCreator.getTimeOrderedEpoch().toString(),
            idMateria,
            nombre,
            universidad
        )
    }

    fun DatosMateria.entidad(): Materia {
        return Materia(
            id,
            idMateria,
            nombre,
            universidad
        )
    }

    fun Materia.modelo(): DatosMateria {
        return DatosMateria(
            id,
            idMateria,
            nombre,
            universidad
        )
    }

    fun crear(nuevaMateria: NuevoMateria): Materia {
        val (idMateria, _, universidad) = nuevaMateria
        if(repositorioMateria.verPorIdPorUniversidad(idMateria, universidad).isNotEmpty()) {
            throw ExcepcionElementoRepetido(
                "Materia",
                "Ya existe una materia con id $idMateria."
            )
        }
        return repositorioMateria.crear(nuevaMateria.entidad())
    }

    @Cacheable("materia_por_universidad", key = "#universidad")
    fun verPorUniversidad(universidad: String): List<DatosMateria> {
        return repositorioMateria.verPorUniversidad(universidad).map { it.modelo() }
    }

    fun actualizar(datosMateria: DatosMateria) {
        repositorioMateria.actualizar(datosMateria.entidad())
    }

    fun borrar(id: String) {
        repositorioMateria.deleteById(id)
    }


    fun verMiUniversidad(): List<DatosMateria> {
        val universidad = servicioContextoSeguridad.datosUsuario.universidad
        return verPorUniversidad(universidad)
    }


}
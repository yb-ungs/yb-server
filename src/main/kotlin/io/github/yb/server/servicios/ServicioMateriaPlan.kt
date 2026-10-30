package io.github.yb.server.servicios

import com.github.f4b6a3.uuid.UuidCreator
import io.github.yb.server.entidades.MateriaPlan
import io.github.yb.server.excepciones.ExcepcionElementoRepetido
import io.github.yb.server.modelos.DatosMateriaPlan
import io.github.yb.server.modelos.NuevoMateriaPlan
import io.github.yb.server.repositorios.RepositorioMateriaPlan
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class ServicioMateriaPlan(
    val repositorioMateriaPlan: RepositorioMateriaPlan,
    val servicioContextoSeguridad: ServicioContextoSeguridad
) {
    fun NuevoMateriaPlan.entidad(): MateriaPlan {
        return MateriaPlan(
            UuidCreator.getTimeOrderedEpoch().toString(),
            idMateria,
            correlativas,
            idPlan,
            cuatrimestre,
            carrera,
            plan,
            universidad
        )
    }

    fun DatosMateriaPlan.entidad(): MateriaPlan {
        return MateriaPlan(
            id,
            idMateria,
            correlativas,
            idPlan,
            cuatrimestre,
            carrera,
            plan,
            universidad
        )
    }

    fun MateriaPlan.modelo(): DatosMateriaPlan {
        return DatosMateriaPlan(
            id,
            idMateria,
            correlativas,
            idPlan,
            cuatrimestre,
            carrera,
            plan,
            universidad
        )
    }

    fun crear(nuevoMateriaPlan: NuevoMateriaPlan): MateriaPlan {
        val (idMateria, _, _, _, carrera, plan, universidad) = nuevoMateriaPlan
        if (repositorioMateriaPlan.verPorMateria(idMateria)
                .any { it.carrera == carrera && it.plan == plan && it.universidad == universidad }
        ) {
            throw ExcepcionElementoRepetido(
                "MateriaPlan",
                "Ya existe una materia con id $idMateria en el plan $plan de la carrera $carrera"
            )
        }
        return repositorioMateriaPlan.crear(nuevoMateriaPlan.entidad())
    }

    fun actualizar(datosMateriaPlan: DatosMateriaPlan) {
        repositorioMateriaPlan.actualizar(datosMateriaPlan.entidad())
    }

    fun borrar(id: String) {
        repositorioMateriaPlan.deleteById(id)
    }

    fun verMiUniversidad(): List<DatosMateriaPlan> {
        val universidad = servicioContextoSeguridad.datosUsuario.universidad
        return verPorUniversidad(universidad)
    }

    @Cacheable("materia_plan_por_universidad", key = "#universidad")
    fun verPorUniversidad(universidad: String): List<DatosMateriaPlan> {
        return repositorioMateriaPlan.verPorUniversidad(universidad).map { it.modelo() }
    }
}
package io.github.yb.server.servicios

import com.github.f4b6a3.uuid.UuidCreator
import io.github.yb.server.entidades.AulaHorario
import io.github.yb.server.modelos.DatosAulaHorario
import io.github.yb.server.modelos.NuevoAulaHorario
import io.github.yb.server.repositorios.RepositorioAulaHorario
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class ServicioAulaHorario(
    val repositorioAulaHorario: RepositorioAulaHorario,
    val servicioContextoSeguridad: ServicioContextoSeguridad
) {

    private fun AulaHorario.modelo(): DatosAulaHorario {
        return DatosAulaHorario(
            id,
            idMateria,
            idAula,
            comision,
            horario,
            universidad,
            creadoEl.toString()
        )
    }

    private fun NuevoAulaHorario.entidad(): AulaHorario {
        return AulaHorario(
            UuidCreator.getTimeOrderedEpoch().toString(),
            idMateria,
            idAula,
            comision,
            horario,
            universidad,
            OffsetDateTime.now()
        )
    }

    private fun DatosAulaHorario.entidad(): AulaHorario {
        return AulaHorario(
            id,
            idMateria,
            idAula,
            comision,
            horario,
            universidad,
            OffsetDateTime.parse(creadoEl)
        )
    }

    fun verPorComisionPorMateriaMiUniversidad(comision: String, idMateria: String): List<DatosAulaHorario>{
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return verPorComisionPorMateriaPorUniversidad(
            comision,
            idMateria,
            datosUsuario.universidad
        )
    }

    @Cacheable(
        "aula_horario_por_comision_por_materia_por_universidad",
        key = "#universidad + '_' + #idMateria + '_' + #comision"
    )
    fun verPorComisionPorMateriaPorUniversidad(
        comision: String,
        idMateria: String,
        universidad: String
    ): List<DatosAulaHorario>{
        return repositorioAulaHorario.verPorComisionPorMateriaPorUniversidad(
            comision,
            idMateria,
            universidad
        ).map { it.modelo() }
    }

    fun crear(nuevoAulaHorario: NuevoAulaHorario): AulaHorario {
        return repositorioAulaHorario.crear(nuevoAulaHorario.entidad())
    }

    fun actualizar(datosAulaHorario: DatosAulaHorario) {
        repositorioAulaHorario.actualizar(datosAulaHorario.entidad())
    }

    fun borrar(id: String) {
        repositorioAulaHorario.deleteById(id)
    }
}
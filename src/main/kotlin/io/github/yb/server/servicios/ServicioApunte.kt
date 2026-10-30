package io.github.yb.server.servicios

import com.github.f4b6a3.uuid.UuidCreator
import io.github.yb.server.entidades.Apunte
import io.github.yb.server.modelos.DatosApunte
import io.github.yb.server.modelos.NuevoApunte
import io.github.yb.server.repositorios.RepositorioApunte
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import org.springframework.cache.annotation.Cacheable

@Service
class ServicioApunte(
    val repositorioApunte: RepositorioApunte,
    val servicioContextoSeguridad: ServicioContextoSeguridad
) {


    private fun NuevoApunte.entidad(): Apunte {
        return Apunte(
            UuidCreator.getTimeOrderedEpoch().toString(),
            idApunte,
            idMateria,
            nombre,
            descripcion,
            ubicacion,
            universidad,
            OffsetDateTime.now()
        )
    }

    private fun Apunte.modelo(): DatosApunte {
        return DatosApunte(
            id,
            idApunte,
            idMateria,
            nombre,
            descripcion,
            ubicacion,
            universidad,
            creadoEl.toString()
        )
    }

    private fun DatosApunte.entidad(): Apunte {
        return Apunte(
            id,
            idApunte,
            idMateria,
            nombre,
            descripcion,
            ubicacion,
            universidad,
            OffsetDateTime.parse(creadoEl)
        )
    }

    fun crear(nuevoApunte: NuevoApunte): Apunte{
        return repositorioApunte.crear(nuevoApunte.entidad())
    }

    fun actualizar(datosApunte: DatosApunte) {
        repositorioApunte.actualizar(datosApunte.entidad())
    }

    fun borrar(id: String) {
        repositorioApunte.deleteById(id)
    }

    fun verMiUniversidad(): List<DatosApunte> {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return verPorUniversidad(datosUsuario.universidad)
    }

    fun verPorMateriaMiUniversidad(idMateria: String): List<DatosApunte> {
        val datosUsuario = servicioContextoSeguridad.datosUsuario
        return verPorMateriaUniversidad(
            idMateria,
            datosUsuario.universidad
        )
    }

    @Cacheable("apunte_por_universidad", key = "#universidad")
    fun verPorUniversidad(universidad: String): List<DatosApunte> {
        return repositorioApunte.encontrarPorUniversidad(universidad).map { it.modelo() }
    }

    @Cacheable("apunte_por_materia_por_universidad", key = "#universidad + '_' + #idMateria")
    fun verPorMateriaUniversidad(idMateria: String, universidad: String): List<DatosApunte> {
        return repositorioApunte.encontrarPorMateriaPorUniversidad(idMateria, universidad).map { it.modelo() }
    }
}
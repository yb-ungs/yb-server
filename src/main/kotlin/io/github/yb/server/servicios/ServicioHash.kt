package io.github.yb.server.servicios

import io.github.yb.server.util.Permiso
import org.springframework.stereotype.Service
import kotlin.math.abs

@Service
class ServicioDeHash {

    private val magicNumber by lazy {
        val nombres = 
        for (m in 0..100000 step 1) {
            val hashesVistos = mutableSetOf<Int>()
            var huboColision = false

            for (nombre in nombres) {
                val h = hash(nombre, m)
                if (!hashesVistos.add(h)) {
                    huboColision = true
                    break
                }
            }

            if (!huboColision) return m
        }
        throw RuntimeException("No se encontró número mágico.")
    }

    private fun buscarNumeroMagico(nombres: List<String>): Int {

    }

    fun hash(nombre: String, magicNumber: Int): Int {
        val modulo = Permiso.entries.size * 2 + 1
        var hash = 0
        for (char in nombre) {
            hash = hash * magicNumber + char.code
        }
        // Usamos rem (módulo) y abs para asegurar el rango [0, 78]
        return abs(hash % modulo)
    }
}
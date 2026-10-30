package io.github.yb.server.servicios

import io.github.yb.server.util.Permiso
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.util.Random
import kotlin.math.abs

@Service
class ServicioHash {

    companion object {
        private val logger = LoggerFactory.getLogger(ServicioHash::class.java)
    }

    val modulo = Permiso.entries.size * 15
    val numeroMagico by lazy {
        val nombres = Permiso.entries.map { it.name }
        var magico = -1
        val rdn = Random()
        while (true) {
            val m = BigInteger.probablePrime(20, rdn).intValueExact()
            val hashesVistos = mutableSetOf<Int>()
            var huboColision = false

            for (nombre in nombres) {
                val h = hash(nombre, m)
                if (!hashesVistos.add(h)) {
                    huboColision = true
                    break
                }
            }
            if (!huboColision){
                magico = m
                break;
            }
        }
        if(magico == -1){
            throw RuntimeException("No se encontró número mágico.")
        }

        logger.info("se creó servicio de hash con numero magico {} y modulo {}", magico, modulo)
        magico
    }

    private fun hash(nombre: String, numeroMagico: Int): Int {

        var hash = 0
        for (char in nombre) {
            hash = hash * numeroMagico + char.code
        }
        return abs(hash % modulo)
    }

    fun hash(nombre: String): Int {
        return hash(nombre, numeroMagico)
    }
}
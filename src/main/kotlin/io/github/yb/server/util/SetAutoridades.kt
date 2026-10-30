package io.github.yb.server.util

import io.github.yb.server.servicios.ServicioHash
import org.springframework.security.core.GrantedAuthority
import kotlin.sequences.forEach
class SetAutoridades(
    autoridades: List<GrantedAuthority>,
    val servicioHash: ServicioHash
) : Collection<GrantedAuthority> {

    private val array = Array<GrantedAuthority?>(servicioHash.modulo) { null }

    init {
        autoridades.asSequence().distinct().forEach { array[servicioHash.hash(it.authority!!)] = it }
    }

    override fun contains(element: GrantedAuthority): Boolean {
        val hash = servicioHash.hash(element.authority!!)
        val elementoEnIndice = array[hash]
        return elementoEnIndice != null && elementoEnIndice.authority == element.authority
    }

    override fun containsAll(elements: Collection<GrantedAuthority>): Boolean {
        for (element in elements) {
            if (!contains(element)) {
                return false
            }
        }
        return true
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun iterator(): Iterator<GrantedAuthority> {
        return IteradorSetAutoridades(array, size)
    }

    override val size = autoridades.size
    
    class IteradorSetAutoridades(
        private val backedArray: Array<GrantedAuthority?>,
        private val size: Int
    ) : Iterator<GrantedAuthority> {

        private var indice = 0
        private var conteo = 0

        init {
            avanzarAlSiguiente()
        }

        private fun avanzarAlSiguiente() {
            while (indice < backedArray.size && backedArray[indice] == null) {
                indice++
            }
        }

        override fun hasNext(): Boolean = (conteo < size) && (indice < backedArray.size)

        override fun next(): GrantedAuthority {
            if (!hasNext()) throw NoSuchElementException()
            val elemento = backedArray[indice]!!
            indice++
            conteo++
            avanzarAlSiguiente()
            return elemento
        }
    }
}
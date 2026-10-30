package io.github.yb.server.excepciones

import kotlin.reflect.KClass

class ExcepcionInicializacion (
    val clase: KClass<*>,
    val parametro: String,
    val causa: String,
) : RuntimeException() {
    override val message: String = """
        Fallo en inicialización
        Clase: ${clase.java.name}
        Parametro: $parametro
        Causa: $causa
    """.trimIndent()
}
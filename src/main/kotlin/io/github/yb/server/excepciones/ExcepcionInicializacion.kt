package io.github.yb.server.excepciones

import kotlin.reflect.KClass

class ExcepcionInicializacion (
    val clase: KClass<*>,
    val parametro: String,
    val causa: String,
) : RuntimeException() {
    override val message: String = """
        Initialization Failed 
        Instance of: ${clase.java.name}
        Parameter: $parametro
        Cause: $causa
    """.trimIndent()
}
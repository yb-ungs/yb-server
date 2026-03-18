package io.github.yb.server.excepciones

class ExcepcionAutentificacion(val causa: String, val origen: String): RuntimeException() {
    override val message: String = """
        Error de autentificación.
        Causa: $causa
        Clase: $origen
    """.trimIndent()
}
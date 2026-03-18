package io.github.yb.server.excepciones

class ExcepcionAutorizacion (val causa: String, val origen: String) : RuntimeException(){
    override val message: String = """
        Error de autorización.
        Causa: $causa
        Clase: $origen
    """.trimIndent()
}
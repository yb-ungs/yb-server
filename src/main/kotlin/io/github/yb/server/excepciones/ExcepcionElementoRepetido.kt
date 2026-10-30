package io.github.yb.server.excepciones

class ExcepcionElementoRepetido(entidad: String, mensaje: String) : Exception() {
    override val message = "Error de elemento repetido para la entidad $entidad. $mensaje"
}
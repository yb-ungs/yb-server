package io.github.yb.server.web

import com.zaxxer.hikari.pool.HikariPool
import io.github.yb.server.excepciones.ExcepcionAutentificacion
import io.github.yb.server.excepciones.ExcepcionAutorizacion
import io.github.yb.server.excepciones.ExcepcionElementoRepetido
import io.github.yb.server.excepciones.ExcepcionInicializacion
import io.micrometer.core.annotation.Counted
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.validation.BindException
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.sql.SQLException
import java.sql.SQLNonTransientConnectionException
import java.sql.SQLTransientConnectionException
import java.sql.SQLTransientException
import java.time.OffsetDateTime

@RestControllerAdvice
class ControladorErrores(
    val meterRegistry: MeterRegistry,
) {


    companion object {
        private val logger = LoggerFactory.getLogger(ControladorErrores::class.java)
    }

    @Counted("illegal.argument.exception")
    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(ex: Exception): ResponseEntity<Any> {
        return handleBadRequest(ex)
    }

    @Counted("illegal.state.exception")
    @ExceptionHandler(
        IllegalStateException::class
    )fun illegalStateException(ex: Exception): ResponseEntity<Any> {
        return handleBadRequest(ex)
    }

    @Counted("no.such.element.exception")
    @ExceptionHandler(
        NoSuchElementException::class
    )fun noSuchElementException(ex: Exception): ResponseEntity<Any> {
        return handleBadRequest(ex)
    }

    @Counted("username.not.found.exception")
    @ExceptionHandler(
        UsernameNotFoundException::class
    )fun usernameNotFoundException(ex: Exception): ResponseEntity<Any> {
        return handleBadRequest(ex)
    }

    @Counted("method.argument.not.valid.exception")
    @ExceptionHandler(
        MethodArgumentNotValidException::class
    )fun methodArgumentNotValidException(ex: Exception): ResponseEntity<Any> {
        return handleBadRequest(ex)
    }

    @Counted("http.message.not.readable.exception")
    @ExceptionHandler(
        HttpMessageNotReadableException::class
    )fun httpMessageNotReadableException(ex: Exception): ResponseEntity<Any> {
        return handleBadRequest(ex)
    }

    @Counted("missing.servlet.request.parameter.exception")
    @ExceptionHandler(
        MissingServletRequestParameterException::class
    )fun missingServletRequestParameterException(ex: Exception): ResponseEntity<Any> {
        return handleBadRequest(ex)
    }

    @Counted("method.argument.type.mismatch.exception")
    @ExceptionHandler(
        MethodArgumentTypeMismatchException::class
    )fun methodArgumentTypeMismatchException(ex: Exception): ResponseEntity<Any> {
        return handleBadRequest(ex)
    }

    @Counted("bind.exception")
    @ExceptionHandler(
        BindException::class
    )fun bindException(ex: Exception): ResponseEntity<Any> {
        return handleBadRequest(ex)
    }

    @Counted("excepcion.autentificacion")
    @ExceptionHandler(
        ExcepcionAutentificacion::class
    )fun excepcionAutentificacion(ex: Exception): ResponseEntity<Any> {
        return handleBadRequest(ex)
    }

    @Counted("excepcion.autorizacion")
    @ExceptionHandler(
        ExcepcionAutorizacion::class
    )fun excepcionAutorizacion(ex: Exception): ResponseEntity<Any> {
        return handleBadRequest(ex)
    }

    fun handleBadRequest(ex: Exception): ResponseEntity<Any> {
        logger.info("manejando error de mala solicitud de tipo {} con mensaje {}", ex::class.simpleName, ex.message)
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST)
    }

    @Counted("sql.exception")
    @ExceptionHandler(SQLException::class)
    fun sqlException(ex: Exception): ResponseEntity<Any> {
        return handleInternalError(ex)
    }

    @Counted("sql.transient.connection.exception")
    @ExceptionHandler(SQLTransientConnectionException::class)
    fun sqlTransientConnectionException(ex: Exception): ResponseEntity<Any> {
        return handleInternalError(ex)
    }

    @Counted("sql.transient.exception")
    @ExceptionHandler(SQLTransientException::class)
    fun sqlTransientException(ex: Exception): ResponseEntity<Any> {
        return handleInternalError(ex)
    }

    @Counted("sql.non.transient.connection.exception")
    @ExceptionHandler(SQLNonTransientConnectionException::class)
    fun sqlNonTransientConnectionException(ex: Exception): ResponseEntity<Any> {
        return handleInternalError(ex)
    }

    @Counted("pool.initialization.exception")
    @ExceptionHandler(HikariPool.PoolInitializationException::class)
    fun poolInitializationException(ex: Exception): ResponseEntity<Any> {
        return handleInternalError(ex)
    }

    @Counted("null.pointer.exception")
    @ExceptionHandler(NullPointerException::class)
    fun nullPointerException(ex: Exception): ResponseEntity<Any> {
        return handleInternalError(ex)
    }

    @Counted("excepcion.inicializacion")
    @ExceptionHandler(ExcepcionInicializacion::class)
    fun excepcionInicializacion(ex: Exception): ResponseEntity<Any> {
        return handleInternalError(ex)
    }

    @Counted("otras.excepciones")
    @ExceptionHandler(Exception::class)
    fun otras(ex: Exception): ResponseEntity<Any> {
        return handleInternalError(ex)
    }

    @Counted("excepcion.elemento.repetido")
    @ExceptionHandler(ExcepcionElementoRepetido::class)
    fun excepcionElementoRepetido(ex: Exception): ResponseEntity<Any> {
        return handleBadRequest(ex)
    }

    fun handleInternalError(ex: Exception): ResponseEntity<Any> {
        logger.info("manejando error interno de servidor de tipo {} con mensaje {}", ex::class.simpleName, ex.message)
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun buildErrorResponse(ex: Exception, status: HttpStatusCode): ResponseEntity<Any> {
        val errorResponse = ErrorResponse.builder(ex, status, ex.message ?: "Sin detalles adicionales")
            .title("Error: ${ex::class.simpleName}")
            .property("timestamp", OffsetDateTime.now())
            .build().also {
                logger.info("se ha creado la instancia de ProblemDetail {} para cumplir con RFC 9457", it.toString())
            }

        return ResponseEntity.status(status).body(errorResponse.body)
    }
}
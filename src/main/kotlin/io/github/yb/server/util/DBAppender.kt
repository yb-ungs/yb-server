package io.github.yb.server.util

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.IThrowableProxy
import ch.qos.logback.core.AppenderBase
import com.github.f4b6a3.uuid.UuidCreator
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.yb.server.servicios.SystemDotenv


class DBAppender : AppenderBase<ILoggingEvent>() {

    val dotenv = SystemDotenv()

    val dataSource by lazy {
        val config = HikariConfig()
        config.jdbcUrl = dotenv.get("DB_URL")
        config.username = dotenv.get("DB_USER")
        config.password = dotenv.get("DB_PASS")
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        HikariDataSource(config)
    }

    override fun append(event: ILoggingEvent?) {
        if (event == null) return

        val sql = """
            INSERT INTO registros (id, creado_el, nivel, nombre, hilo, mensaje, traza)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        try {
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, UuidCreator.getTimeOrderedEpoch().toString())
                    stmt.setObject(2, java.time.OffsetDateTime.ofInstant(
                        java.time.Instant.ofEpochMilli(event.timeStamp),
                        java.time.ZoneId.systemDefault()
                    ))
                    stmt.setString(3, event.level.toString())
                    stmt.setString(4, event.loggerName)
                    stmt.setString(5, event.threadName)
                    stmt.setString(6, event.formattedMessage)
                    stmt.setString(7, event.throwableProxy?.toStackTraceString())
                    stmt.executeUpdate()
                }
            }
        } catch (e: Exception) {
            addError("Error al insertar log en BD", e)
        }
    }

    private fun IThrowableProxy.toStackTraceString(): String = buildString {
        appendLine("$className: $message")
        stackTraceElementProxyArray?.forEach { appendLine("\tat $it") }
        cause?.let {
            append("Caused by: ")
            append(it.toStackTraceString())
        }
    }
}
package io.github.yb.server

import io.github.yb.server.servicios.SystemDotenv
import org.slf4j.LoggerFactory
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class ConfiguracionBaseDeDatos(val dotenv: SystemDotenv) {
    companion object {
        private val logger = LoggerFactory.getLogger(ConfiguracionBaseDeDatos::class.java)
    }
    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(dotenv.get("DB_URL")!!)
            .username(dotenv.get("DB_USER")!!)
            .password(dotenv.get("DB_PASS")!!)
            .build()
            .also {
                logger.info("Datasource creada.")
            }
    }
}
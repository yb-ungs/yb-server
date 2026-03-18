package io.github.yb.server.servicios

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvEntry
import io.github.cdimascio.dotenv.dotenv
import org.springframework.stereotype.Component

@Component
class SystemDotenv() : Dotenv {
    private val backed: Dotenv = dotenv { ignoreIfMissing = true }

    override fun entries(): Set<DotenvEntry> {
        throw UnsupportedOperationException()
    }

    override fun entries(filter: Dotenv.Filter): Set<DotenvEntry> {
        throw UnsupportedOperationException()
    }

    override fun get(key: String): String? {
        return System.getenv(key) ?: backed.get(key)
    }

    override fun get(key: String, defaultValue: String): String {
        return get(key) ?: defaultValue
    }
}
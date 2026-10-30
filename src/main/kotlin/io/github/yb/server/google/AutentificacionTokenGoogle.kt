package io.github.yb.server.google

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class GoogleAuthenticationToken(
    private val idToken: String,
    val principal: Any? = null,
    autoridades: Collection<GrantedAuthority> = emptyList()
) : AbstractAuthenticationToken(autoridades) {
    init {
        if (principal != null) isAuthenticated = true
    }

    override fun getCredentials() = idToken
    override fun getPrincipal(): Any? {
        return principal
    }
}
package io.github.yb.server.google

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AutentificacionTokenGoogle(
    private val idToken: String,
    private val principalInterno: Any? = null,
    autoridades: Collection<GrantedAuthority> = emptyList()
): AbstractAuthenticationToken(autoridades) {
    init {
        if (principalInterno != null) isAuthenticated = true
    }

    override fun getCredentials() = idToken
    override fun getPrincipal(): Any? {
        return principalInterno
    }
}
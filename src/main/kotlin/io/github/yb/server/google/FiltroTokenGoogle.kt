package io.github.yb.server.google

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class GoogleTokenFilter(
    private val authenticationManager: AuthenticationManager
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val googleHeader = request.getHeader("X-Google-Auth")

        if (googleHeader != null && SecurityContextHolder.getContext().authentication == null) {
            val authRequest = GoogleAuthenticationToken(googleHeader)
            val authResult = authenticationManager.authenticate(authRequest)
            SecurityContextHolder.getContext().authentication = authResult
        }

        chain.doFilter(request, response)
    }
}
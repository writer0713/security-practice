package com.writer0713.practice.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

class CustomAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder,
) : AuthenticationProvider {
    companion object {
        private val log = KotlinLogging.logger { }
    }

    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()

        val userDetails = userDetailsService.loadUserByUsername(username)

        if (passwordEncoder.matches(password, userDetails.password)) {
            log.info { "Authentication successful for user: $username" }
            return UsernamePasswordAuthenticationToken(userDetails, password, userDetails.authorities)
        }

        throw AuthenticationCredentialsNotFoundException("Error in authentication!")
    }

    override fun supports(authentication: Class<*>?): Boolean = authentication == UsernamePasswordAuthenticationToken::class.java
}

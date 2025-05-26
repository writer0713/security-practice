package com.writer0713.practice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig {
    @Bean
    fun filterChain(
        http: HttpSecurity,
        customAuthenticationProvider: AuthenticationProvider,
    ): SecurityFilterChain {
        http.authorizeHttpRequests { auth ->
            auth.anyRequest().permitAll()
        }

        http.formLogin(withDefaults())

        http.authenticationProvider(customAuthenticationProvider)

        return http.build()
    }

//    @Bean
//    fun customAuthenticationManager(
//        http: HttpSecurity,
//        customUserDetailsService: UserDetailsService,
//        customPasswordEncoder: PasswordEncoder
//    ): AuthenticationManager {
//        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
//
//        authenticationManagerBuilder
//            .userDetailsService(customUserDetailsService)
//            .passwordEncoder(customPasswordEncoder)
//
//        return authenticationManagerBuilder.build()
//    }

    @Bean
    fun customAuthenticationProvider(
        customUserDetailsService: UserDetailsService,
        customPasswordEncoder: PasswordEncoder,
    ): AuthenticationProvider =
        CustomAuthenticationProvider(
            customUserDetailsService,
            customPasswordEncoder,
        )

    @Bean
    fun customUserDetailsService(): UserDetailsService {
        val userDetailsService = InMemoryUserDetailsManager()

        val user =
            User
                .withUsername("user")
                .password("12345")
                .authorities("read")
                .build()

        userDetailsService.createUser(user)

        return userDetailsService
    }

    @Bean
    fun customPasswordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
//        return BCryptPasswordEncoder()
    }
}

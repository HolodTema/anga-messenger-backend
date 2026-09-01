package com.terabyte.angamessenger.chat.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity() {
    val jwtSecret = environment.config.property("jwt.secret").getString()
    install(Authentication) {
        jwt("auth-jwt") {
            val jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build()
            verifier(jwtVerifier)
            validate { credential ->
                val userId = credential.payload.subject?.toLongOrNull()
                if (userId != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

package com.terabyte.angamessenger.auth.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.terabyte.angamessenger.common.security.JwtConfig
import com.terabyte.angamessenger.common.security.JwtService
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.util.AttributeKey
import java.time.Duration

val JwtServiceKey = AttributeKey<JwtService>("JwtService")
val JwtConfigKey = AttributeKey<JwtConfig>("JwtConfig")

fun Application.configureSecurity() {
    val jwtSecret = environment.config.property("jwt.secret").getString()
    val accessTokenTimeToLive = Duration.parse(environment.config.property("jwt.accessTimeToLive").getString())
    val refreshTokenTimeToLive = Duration.parse(environment.config.property("jwt.refreshTimeToLive").getString())

    val jwtConfig = JwtConfig(jwtSecret, accessTokenTimeToLive, refreshTokenTimeToLive)
    val jwtService = JwtService(jwtConfig)

    // there is a listener: every time when the ktor application is started, we put some objects into attributes store
    // Application.monitor is a special object, which monitors ktor-events and can create listeners
    monitor.subscribe(ApplicationStarted) {
        // attributes is runtime store for our Ktor Application
        // we can save any object into attributes store and get these objects from another part of the app easily
        // to do this, we created AttributeKey objects above
        it.attributes.put(JwtServiceKey, jwtService)
        it.attributes.put(JwtConfigKey, jwtConfig)
    }

    // Authentication - built-in Ktor plugin to auth some model objects using JWT
    install(Authentication) {
        // auth-jwt is our custom name to recognize certain jwt-verifier in routing plugin later
        jwt("auth-jwt") {
            // jwtVerifier is an object which can check JWT-token timeToLive and authenticity
            val jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build()
            // the line below we say to built-in ktor plugin: "Use our jwtVerifier object as your verifier"
            verifier(jwtVerifier)
            validate { credential ->
                // credential is an object, which stores JWT-token's payload
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

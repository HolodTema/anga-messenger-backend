package com.terabyte.angamessenger.auth

import com.terabyte.angamessenger.auth.plugins.configureDatabase
import com.terabyte.angamessenger.auth.plugins.configureSecurity
import com.terabyte.angamessenger.auth.plugins.configureSerialization
import com.terabyte.angamessenger.auth.plugins.configureStatusPages
import com.terabyte.angamessenger.auth.routes.authRoutes
import com.terabyte.angamessenger.auth.routes.userRoutes
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import io.ktor.server.routing.routing

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureDatabase()
    configureSerialization()
    configureSecurity()
    configureStatusPages()
    routing {
        authRoutes()
        userRoutes()
    }
}

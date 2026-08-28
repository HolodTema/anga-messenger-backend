package com.terabyte.angamessenger.auth

import com.terabyte.angamessenger.auth.plugins.configureDatabase
import com.terabyte.angamessenger.auth.plugins.configureSecurity
import com.terabyte.angamessenger.auth.plugins.configureSerialization
import com.terabyte.angamessenger.auth.plugins.configureStatusPages
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDatabase()
    configureSerialization()
    configureSecurity()
    configureStatusPages()
    routing {
    }
}

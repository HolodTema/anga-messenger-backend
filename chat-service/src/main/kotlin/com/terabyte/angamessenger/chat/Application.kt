package com.terabyte.angamessenger.chat

import com.terabyte.angamessenger.chat.plugins.configureDatabase
import com.terabyte.angamessenger.chat.plugins.configureSecurity
import com.terabyte.angamessenger.chat.plugins.configureSerialization
import com.terabyte.angamessenger.chat.plugins.configureStatusPages
import com.terabyte.angamessenger.chat.plugins.configureWebSockets
import com.terabyte.angamessenger.chat.routes.chatRoutes
import com.terabyte.angamessenger.chat.routes.messageRoutes
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
    configureWebSockets()
    routing {
        chatRoutes()
        messageRoutes()
    }
}

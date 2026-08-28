package com.terabyte.angamessenger.chat.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import kotlin.time.Duration.Companion.seconds

fun Application.configureWebSockets() {
    install(WebSockets) {
        // how often server needs to send ping-frame to the client to check if the connection is alive
        pingPeriod = 60.seconds

        // if the server cannot get pong-frame from the client during timeout, server will close the connection
        timeout = 15.seconds

        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}

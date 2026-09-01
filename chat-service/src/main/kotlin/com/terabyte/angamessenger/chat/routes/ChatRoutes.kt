package com.terabyte.angamessenger.chat.routes

import com.terabyte.angamessenger.chat.websocket.WebSocketSessionManager
import com.terabyte.angamessenger.common.security.JwtConfig
import com.terabyte.angamessenger.common.security.JwtService
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import java.time.Duration

fun Route.chatRoutes() {
    val jwtSecret = environment.config.property("jwt.secret").getString()
    val jwtService = JwtService(JwtConfig(jwtSecret, Duration.ZERO, Duration.ZERO))

    webSocket("/chat/ws") {
        val token = call.request.queryParameters["token"]
        if (token.isNullOrBlank()) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Error: no accessToken in query params"))
            return@webSocket
        }

        val userId = jwtService.verifyAccessToken(token)
        if (userId == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Error: invalid accessToken"))
            return@webSocket
        }

        WebSocketSessionManager.addSession(userId, this)

        try {
            for (frame in incoming) {
                // do nothing, just wait while all the messages are over
            }
        } finally {
            // when all the messages are over, we can close this session
            WebSocketSessionManager.removeSession(userId, this)
        }
    }
}

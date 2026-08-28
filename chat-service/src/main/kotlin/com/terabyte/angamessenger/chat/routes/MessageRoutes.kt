package com.terabyte.angamessenger.chat.routes

import com.terabyte.angamessenger.chat.dao.MessageDao
import com.terabyte.angamessenger.chat.dao.UserDao
import com.terabyte.angamessenger.chat.websocket.WebSocketSessionManager
import com.terabyte.angamessenger.common.model.SendMessageRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.serialization.json.Json

fun Route.messageRoutes() {
    authenticate("auth-jwt") {
        post("/message") {
            val senderId = call.principal<JWTPrincipal>()?.payload?.subject?.toLongOrNull()
            if (senderId == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            val request = call.receive<SendMessageRequest>()
            if (request.text.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Error: message text cannot be blank")
                return@post
            }

            if (!UserDao.isUserExists(request.recipientId)) {
                call.respond(HttpStatusCode.BadRequest, "Error: user-recipient does not exist")
                return@post
            }

            val messageId = MessageDao.createMessage(senderId, request.recipientId, request.text)
            val message =
                MessageDao.getMessagesBetween(senderId, request.recipientId, 1, 0)
                    .firstOrNull()
            if (message == null) {
                call.respond(HttpStatusCode.InternalServerError, "Error: server cannot save message due to internal error")
                return@post
            }

            val jsonMessage = Json.encodeToString(message)
            WebSocketSessionManager.sendToUser(request.recipientId, jsonMessage)
            call.respond(HttpStatusCode.Created, message)
        }

        get("/message") {
            val userId1 = call.principal<JWTPrincipal>()?.payload?.subject?.toLongOrNull()
            call.queryParameters["re"]
            if (userId1 == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val userId2 = call.queryParameters["userId"]?.toLongOrNull()
            if (userId2 == null) {
                call.respond(HttpStatusCode.BadRequest, "Error: missing userId query parameter")
                return@get
            }

            val limit = call.queryParameters["limit"]?.toIntOrNull() ?: 50
            val offset = call.queryParameters["offset"]?.toLongOrNull() ?: 0

            val listMessages = MessageDao.getMessagesBetween(userId1, userId2, limit, offset)
            call.respond(HttpStatusCode.OK, listMessages)
        }
    }
}

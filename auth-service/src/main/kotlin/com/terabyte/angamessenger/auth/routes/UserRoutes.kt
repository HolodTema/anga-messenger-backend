package com.terabyte.angamessenger.auth.routes

import com.terabyte.angamessenger.auth.dao.UserDao
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.userRoutes() {
    authenticate("auth-jwt") {
        get("/user/me") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.subject?.toLongOrNull()
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val user = UserDao.getById(userId)
            if (user == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, user)
            }
        }
        get("user/search") {
            val nicknameQuery = call.request.queryParameters["nicknameQuery"] ?: ""
            if (nicknameQuery.length <= 2) {
                call.respond(HttpStatusCode.BadRequest, "Error: nickname query to search is too short")
                return@get
            }
            val listUserPublic = UserDao.searchByNickname(nicknameQuery)
            call.respond(HttpStatusCode.OK, listUserPublic)
        }
    }
}

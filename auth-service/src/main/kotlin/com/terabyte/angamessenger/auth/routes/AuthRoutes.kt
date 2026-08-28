package com.terabyte.angamessenger.auth.routes

import com.terabyte.angamessenger.auth.dao.RefreshTokenDao
import com.terabyte.angamessenger.auth.dao.UserDao
import com.terabyte.angamessenger.auth.plugins.JwtConfigKey
import com.terabyte.angamessenger.auth.plugins.JwtServiceKey
import com.terabyte.angamessenger.common.model.UserAuthResponse
import com.terabyte.angamessenger.common.model.UserRegisterRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.mindrot.jbcrypt.BCrypt
import java.time.Instant

fun Route.authRoutes() {
    val application = this@authRoutes.application

    route("/auth") {
        post("/register") {
            val request = call.receive<UserRegisterRequest>()
            if (request.nickname.isBlank() || request.password.length < 8) {
                call.respond(HttpStatusCode.BadRequest, "Error: invalid nickname or password")
                return@post
            }

            val existingUser = UserDao.getByNickname(request.nickname)
            if (existingUser != null) {
                call.respond(HttpStatusCode.Conflict, "Errror: this nickname is busy")
                return@post
            }

            val hashedPassword = BCrypt.hashpw(request.password, BCrypt.gensalt())
            val userId = UserDao.createUser(request.nickname, request.firstName, request.lastName, hashedPassword)

            val jwtService = application.attributes[JwtServiceKey]
            val jwtConfig = application.attributes[JwtConfigKey]

            val accessToken = jwtService.generateAccessToken(userId)
            val refreshToken = jwtService.generateRefreshToken(userId)
            val refreshTokenExpiresAt = Instant.now().plus(jwtConfig.refreshTimeToLive)
            RefreshTokenDao.saveRefreshToken(userId, refreshToken, refreshTokenExpiresAt)

            call.respond(HttpStatusCode.Created, UserAuthResponse(accessToken, refreshToken, userId))
        }
    }
}

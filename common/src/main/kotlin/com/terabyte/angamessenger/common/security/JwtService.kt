package com.terabyte.angamessenger.common.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.Duration
import java.time.Instant
import java.util.Date

data class JwtConfig(
    val secret: String,
    val accessTimeToLive: Duration,
    val refreshTimeToLive: Duration,
)

class JwtService(private val config: JwtConfig) {
    private val algorithm = Algorithm.HMAC256(config.secret)

    fun generateAccessToken(userId: Long): String {
        val dateExpiresAt = Date.from(Instant.now().plus(config.accessTimeToLive))
        return JWT.create()
            .withSubject(userId.toString())
            .withExpiresAt(dateExpiresAt)
            .withClaim("type", "access")
            .sign(algorithm)
    }

    fun generateRefreshToken(userId: Long): String {
        val dateExpiresAt = Date.from(Instant.now().plus(config.refreshTimeToLive))
        return JWT.create()
            .withSubject(userId.toString())
            .withExpiresAt(dateExpiresAt)
            .withClaim("type", "refresh")
            .sign(algorithm)
    }

    fun verifyAccessToken(token: String): Long? {
        return try {
            val jwtVerifier = JWT.require(algorithm).build()
            val decodedJwt = jwtVerifier.verify(token)
            if (decodedJwt.getClaim("type").asString() != "access") {
                return null
            }
            decodedJwt.subject?.toLong()
        } catch (e: Exception) {
            null
        }
    }

    fun verifyRefreshToken(token: String): Long? {
        return try {
            val jwtVerifier = JWT.require(algorithm).build()
            val decodedJwt = jwtVerifier.verify(token)
            if (decodedJwt.getClaim("type").asString() != "refresh") {
                return null
            }
            decodedJwt.subject?.toLong()
        } catch (e: Exception) {
            null
        }
    }
}

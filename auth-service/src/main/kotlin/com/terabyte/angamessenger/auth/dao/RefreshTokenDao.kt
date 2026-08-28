package com.terabyte.angamessenger.auth.dao

import com.terabyte.angamessenger.common.table.RefreshTokens
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

object RefreshTokenDao {
    fun saveRefreshToken(
        userId: Long,
        token: String,
        expiresAt: Instant,
    ) {
        transaction {
            RefreshTokens.insert {
                it[RefreshTokens.userId] = userId
                it[RefreshTokens.token] = token
                it[RefreshTokens.expiresAt] = expiresAt
            }
        }
    }

    fun getUserIdByRefreshToken(token: String): Long? {
        return transaction {
            RefreshTokens.select { RefreshTokens.token eq token }
                .map { row ->
                    val expiresAt = row[RefreshTokens.expiresAt]
                    if (expiresAt.isAfter(Instant.now())) row[RefreshTokens.userId] else null
                }
                .singleOrNull()
        }
    }

    fun deleteRefreshToken(token: String) {
        transaction {
            RefreshTokens.deleteWhere { RefreshTokens.token eq token }
        }
    }
}

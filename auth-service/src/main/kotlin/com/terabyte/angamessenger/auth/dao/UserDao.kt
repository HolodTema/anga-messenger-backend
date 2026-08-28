package com.terabyte.angamessenger.auth.dao

import com.terabyte.angamessenger.common.model.User
import com.terabyte.angamessenger.common.model.UserPublic
import com.terabyte.angamessenger.common.table.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object UserDao {
    fun createUser(
        nickname: String,
        firstName: String,
        lastName: String,
        passwordHash: String,
    ): Long =
        transaction {
            val createdUser =
                Users.insert {
                    it[Users.nickname] = nickname
                    it[Users.firstName] = firstName
                    it[Users.lastName] = lastName
                    it[Users.passwordHash] = passwordHash
                }
            createdUser[Users.id]
        }

    fun getByNickname(nickname: String): User? {
        return transaction {
            Users.select { Users.nickname eq nickname }
                .map { toUser(it) }
                .singleOrNull()
        }
    }

    fun getById(id: Long): User? {
        return transaction {
            Users.select { Users.id eq id }
                .map { toUser(it) }
                .singleOrNull()
        }
    }

    fun searchByNickname(
        nicknameQuery: String,
        limit: Int = 20,
    ): List<UserPublic> {
        return transaction {
            Users.select { Users.nickname.lowerCase() like "%${nicknameQuery.lowercase()}%" }
                .limit(limit)
                .map { toUserPublic(it) }
        }
    }

    private fun toUser(row: ResultRow): User {
        return User(
            id = row[Users.id],
            nickname = row[Users.nickname],
            firstName = row[Users.firstName],
            lastName = row[Users.lastName],
            passwordHash = row[Users.passwordHash],
        )
    }

    private fun toUserPublic(row: ResultRow): UserPublic {
        return UserPublic(
            id = row[Users.id],
            nickname = row[Users.nickname],
        )
    }
}

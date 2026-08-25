package com.terabyte.angamessenger.common.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object RefreshTokens : Table("refresh_tokens") {
    val id = long("id").autoIncrement()
    val userId = long("user_id").references(Users.id)
    val token = varchar("token", 512).uniqueIndex()
    val expiresAt = datetime("expires_at")

    override val primaryKey = PrimaryKey(id)
}

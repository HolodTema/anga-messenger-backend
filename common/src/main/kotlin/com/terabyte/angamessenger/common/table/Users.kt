package com.terabyte.angamessenger.common.table

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val id = long("id").autoIncrement()
    val nickname = varchar("nickname", 64).uniqueIndex()
    val firstName = varchar("first_name", 64)
    val lastName = varchar("last_name", 64)
    val passwordHash = varchar("password_hash", 255)

    override val primaryKey = PrimaryKey(id)
}

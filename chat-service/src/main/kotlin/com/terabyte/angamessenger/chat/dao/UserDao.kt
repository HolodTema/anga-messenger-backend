package com.terabyte.angamessenger.chat.dao

import com.terabyte.angamessenger.common.table.Users
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object UserDao {
    fun isUserExists(userId: Long): Boolean {
        return transaction {
            Users.select { Users.id eq userId }.empty().not()
        }
    }
}

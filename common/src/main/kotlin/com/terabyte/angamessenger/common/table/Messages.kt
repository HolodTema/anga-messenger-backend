package com.terabyte.angamessenger.common.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object Messages : Table("messages") {
    val id = long("id").autoIncrement()
    val senderId = long("sender_id").references(Users.id)
    val recipientId = long("recipient_id").references(Users.id)
    val text = text("text")
    val timestamp = timestamp("timestamp")

    override val primaryKey = PrimaryKey(id)
}

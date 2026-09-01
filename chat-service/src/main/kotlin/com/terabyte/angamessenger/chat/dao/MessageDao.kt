package com.terabyte.angamessenger.chat.dao

import com.terabyte.angamessenger.common.model.Message
import com.terabyte.angamessenger.common.table.Messages
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

object MessageDao {
    fun createMessage(
        senderId: Long,
        recipientId: Long,
        text: String,
    ): Long {
        return transaction {
            val insertedMessage =
                Messages.insert {
                    it[Messages.senderId] = senderId
                    it[Messages.recipientId] = recipientId
                    it[Messages.text] = text
                    it[Messages.timestamp] = Instant.now()
                }
            insertedMessage[Messages.id]
        }
    }

    // returns list of messages from the chat between two users
    // the size of the list = limit
    // offset is what number of messages we need to skip since the end of the chat
    fun getMessagesBetween(
        userId1: Long,
        userId2: Long,
        limit: Int = 50,
        offset: Long = 0,
    ): List<Message> {
        return transaction {
            Messages.select {
                (Messages.senderId eq userId1) and
                    (Messages.recipientId eq userId2)
                        .or((Messages.senderId eq userId2) and (Messages.recipientId eq userId1))
            }
                .orderBy(Messages.timestamp to SortOrder.DESC)
                .limit(limit, offset)
                .map {
                    toMessage(it)
                }
                .reversed()
        }
    }

    private fun toMessage(row: ResultRow): Message {
        return Message(
            id = row[Messages.id],
            senderId = row[Messages.senderId],
            recipientId = row[Messages.recipientId],
            text = row[Messages.text],
            timestamp = row[Messages.timestamp],
        )
    }
}

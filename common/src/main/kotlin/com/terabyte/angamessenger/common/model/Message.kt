package com.terabyte.angamessenger.common.model

import java.time.Instant

data class Message(
    val id: Long,
    val senderId: Long,
    val recipientId: Long,
    val text: String,
    val timestamp: Instant,
)

data class SendMessageRequest(
    val recipientId: Long,
    val text: String,
)

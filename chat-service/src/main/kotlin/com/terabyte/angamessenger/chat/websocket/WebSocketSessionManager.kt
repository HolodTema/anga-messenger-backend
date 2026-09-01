package com.terabyte.angamessenger.chat.websocket

import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.send
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap

object WebSocketSessionManager {
    // Here we use ConcurrentHashMap - thread-safe HashMap object.
    // We try to avoid race condition and UB, because mapSession may work inside many coroutines
    private val mapSessions = ConcurrentHashMap<Long, MutableList<DefaultWebSocketServerSession>>()

    // mutex object to sync kotlin coroutines and to make add-remove-websocket-session operations atomic
    private val mutex = Mutex()

    suspend fun addSession(
        userId: Long,
        session: DefaultWebSocketServerSession,
    ) {
        mutex.withLock {
            mapSessions.getOrPut(userId) {
                mutableListOf()
            }.add(session)
        }
    }

    suspend fun removeSession(
        userId: Long,
        session: DefaultWebSocketServerSession,
    ) {
        mutex.withLock {
            mapSessions[userId]?.remove(session)
            if (mapSessions[userId].isNullOrEmpty()) {
                mapSessions.remove(userId)
            }
        }
    }

    suspend fun sendToUser(
        userId: Long,
        message: String,
    ) {
        val listUserSessions = mapSessions[userId] ?: return
        for (session in listUserSessions) {
            try {
                session.send(message)
            } catch (e: Exception) {
                // do nothing
                // log this in the future
            }
        }
    }
}

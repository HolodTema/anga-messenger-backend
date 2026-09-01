package com.terabyte.angamessenger.chat.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

// StatusPages is built-in Ktor plugin to handle backend exceptions and bad HTTP-status-codes in one way
// if some exception happens, our backend can send custom response instead of plain response
fun Application.configureStatusPages() {
    install(StatusPages) {
        // we are going to handle all the exceptions (because of all the exceptions are inherited from Throwable)
        exception<Throwable> { call, cause ->
            // if any exception happens, we send to frontend JSON response with field "error" and status-code 500
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to cause.message))
        }
    }
}

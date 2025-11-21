package dev.ixor.callie.routes.sse

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.sse.*

fun Application.registerSSERoutes() {
    routing {
        sse("/hello") {
            send(ServerSentEvent("World!"))
        }
    }
}

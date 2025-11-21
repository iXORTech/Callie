package dev.ixor.callie.routes.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.registerAPIRoutes() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }

        serializationDemo()
    }
}

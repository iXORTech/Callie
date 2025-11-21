package dev.ixor.callie.routes.api

import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.serializationDemo() {
    get("/json/kotlinx-serialization") {
        call.respond(mapOf("hello" to "world"))
    }
}

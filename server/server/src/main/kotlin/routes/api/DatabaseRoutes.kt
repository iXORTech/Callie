package dev.ixor.callie.routes.api

import dev.ixor.callie.database.ExposedUser
import dev.ixor.callie.plugins.DatabaseServices
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.registerDatabaseRoutes() {
    val userService = DatabaseServices.userService
    routing {
        // Create user
        post("/users") {
            if (userService == null) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Could not create new user. Database service is not available."
                )
                return@post
            }
            val user = call.receive<ExposedUser>()
            val id = userService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }

        // Read user
        get("/users/{id}") {
            if (userService == null) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Could not read user. Database service is not available."
                )
                return@get
            }
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Update user
        put("/users/{id}") {
            if (userService == null) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Could not update user. Database service is not available."
                )
                return@put
            }
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<ExposedUser>()
            userService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }

        // Delete user
        delete("/users/{id}") {
            if (userService == null) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Could not delete user. Database service is not available."
                )
                return@delete
            }
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            userService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}

package dev.ixor.callie.routes.web

import dev.ixor.callie.plugins.respondCss
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.*

fun Application.registerResourcesRoutes() {
    routing {
        // WebJars
        get("/webjars") {
            val webjars = listOf(
                "/webjars/htmx.org/dist/htmx.js",
                "/webjars/htmx-ext-ws/dist/ws.js",
            )
            call.respondText(webjars.joinToString("\n") {
                "<script src='$it'></script>"
            }, ContentType.Text.Html)
        }

        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
        staticResources("/", "/web")

        // CSS DSL
        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.darkBlue
                    margin = Margin(0.px)
                }
                rule("h1.page-title") {
                    color = Color.white
                }
            }
        }
    }
}

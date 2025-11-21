package dev.ixor.callie.routes.web

import dev.ixor.callie.pages.leaderboardPage
import dev.ixor.callie.pages.randomRows
import dev.ixor.callie.plugins.IndexData
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.html.*
import kotlin.random.Random

fun Application.registerPagesRoutes() {
    routing {
        get("/html-css-dsl") {
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                }
                body {
                    h1(classes = "page-title") {
                        +"Hello from Ktor!"
                    }
                }
            }
        }

        get("/html-freemarker") {
            call.respond(
                FreeMarkerContent(
                    "index.ftl",
                    mapOf("data" to IndexData(listOf(1, 2, 3))), "")
            )
        }

        val random = Random(System.currentTimeMillis())
        get("/leaderboard") {
            call.respondHtml {
                leaderboardPage(random)
            }
        }
        get("/more-rows") {
            call.respondHtml {
                body {
                    table {
                        tbody {
                            randomRows(random)
                        }
                    }
                }
            }
        }

        @OptIn(ExperimentalKtorApi::class)
        get("/time") {
            call.respondHtml {
                head {
                    title("HTMX WS Example")
                    script(src = "/webjars/htmx.org/dist/htmx.js") {}
                    script(src = "/webjars/htmx-ext-ws/dist/ws.js") {}
                }
                body {
                    h1 {
                        +"Live Time"
                    }
                    div {
                        attributes["hx-ext"] = "ws"
                        attributes["ws-connect"] = "/ws/time"
                        div {
                            id = "time-display"
                            +"Connecting..."
                        }
                    }
                }
            }
        }
    }
}

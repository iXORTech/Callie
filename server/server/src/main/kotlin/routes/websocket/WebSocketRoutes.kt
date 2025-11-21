package dev.ixor.callie.routes.websocket

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

fun Application.registerWebSocketRoutes() {
    routing {
        webSocket("/ws") {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    outgoing.send(Frame.Text("YOU SAID: $text"))
                    if (text.equals("bye", ignoreCase = true)) {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                    }
                }
            }
        }

        webSocket("/ws/time") {
            log.info("New WebSocket connection established.")
            while (true) {
                val currentTime = """
                    <div hx-swap-oob="innerHTML:#time-display">
                        <p>Current time: ${SimpleDateFormat("HH:mm:ss.SSS").format(Date())}</p>
                    </div>
                    """.trimIndent()
                outgoing.send(Frame.Text(currentTime))
                delay(1000) // Send every second
            }
        }
    }
}

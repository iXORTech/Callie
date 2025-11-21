package dev.ixor.callie

import dev.ixor.callie.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureMonitoring()
    configureSerialization()
    configureTemplating()
    configureDatabases()
    configureSockets()
    // Always configure routing last to make sure all plugins are ready.
    configureRouting()
}

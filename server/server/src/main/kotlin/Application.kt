package dev.ixor.callie

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureMonitoring()
    configureTemplating()
    configureDatabases()
    configureSockets()
    configureRouting()
}

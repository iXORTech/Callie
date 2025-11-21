package dev.ixor.callie.plugins

import dev.ixor.callie.database.UserService
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

class DatabaseServices() {
    companion object {
        var userService: UserService? = null
    }
}

fun Application.configureDatabases() {
    val development = environment.config.propertyOrNull("ktor.development")?.getString()?.toBoolean() ?: false
    val database = if (development) {
        log.warn("Running in development mode, using H2 database.")
        Database.connect(
            driver = "org.h2.Driver",
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            user = "root",
            password = "",
        )
    } else {
        val dbConfig = environment.config.config("database")
        val dbDriver = dbConfig.property("driverClassName").getString()
        val dbUrl = dbConfig.property("jdbcUrl").getString()
        val dbUser = dbConfig.property("username").getString()
        val dbPassword = dbConfig.property("password").getString()
        Database.connect(
            driver = dbDriver,
            url = dbUrl,
            user = dbUser,
            password = dbPassword,
        )
    }
    DatabaseServices.userService = UserService(database)
}

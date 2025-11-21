package dev.ixor.callie.plugins

import dev.ixor.callie.routes.api.registerAPIRoutes
import dev.ixor.callie.routes.api.registerDatabaseRoutes
import dev.ixor.callie.routes.security.registerAuthenticationRoutes
import dev.ixor.callie.routes.sse.registerSSERoutes
import dev.ixor.callie.routes.web.registerPagesRoutes
import dev.ixor.callie.routes.web.registerResourcesRoutes
import dev.ixor.callie.routes.websocket.registerWebSocketRoutes
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.sse.*
import io.ktor.server.webjars.*

fun Application.configureRouting() {
    install(RequestValidation) {
        validate<String> { bodyText ->
            if (!bodyText.startsWith("Hello"))
                ValidationResult.Invalid("Body text should start with 'Hello'")
            else ValidationResult.Valid
        }
    }
    install(SSE)
    install(Webjars) {
        path = "/webjars" //defaults to /webjars
    }

    registerAPIRoutes()
    registerDatabaseRoutes()
    registerAuthenticationRoutes()
    registerSSERoutes()
    registerPagesRoutes()
    registerResourcesRoutes()
    registerWebSocketRoutes()
}

package dev.ixor.callie.plugins

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

fun Application.configureSecurity() {
    authentication {
        basic(name = "basic-auth") {
            realm = "Ktor Server"
            validate { credentials ->
                if (credentials.name == credentials.password) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }

        form(name = "form-auth") {
            userParamName = "user"
            passwordParamName = "password"
            challenge {
                /**/
            }
        }

        oauth("auth-oauth-google") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile")
                )
            }
            client = HttpClient(Apache)
        }
    }

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
}

@Serializable
data class MySession(val count: Int = 0)

class UserSession(accessToken: String)

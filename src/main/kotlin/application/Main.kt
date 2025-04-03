package application

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.http.*
import routes.authRoutes
import routes.analysisRoutes
import utils.TokenManager.verifier

fun module(app: Application) {
    app.apply {
        install(ContentNegotiation) {
            json()
        }

        install(CORS) {
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Get)
            allowHeader(HttpHeaders.Authorization)
            allowHeader(HttpHeaders.ContentType)
            anyHost()
        }

        install(Authentication) {
            jwt {
                verifier(verifier)
                validate { credential ->
                    if (credential.payload.getClaim("username").asString() != "") {
                        JWTPrincipal(credential.payload)
                    } else null
                }
            }
        }

        routing {
            authRoutes()
            analysisRoutes()
        }
    }
}


fun main() {
    DatabaseFactory.init()
    embeddedServer(Netty, port = 8081, host = "0.0.0.0") {
        module(this)  // this вместо it
    }.start(wait = true)
}
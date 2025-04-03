package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import tables.Users
import utils.TokenManager

fun Route.authRoutes() {

    post("/register") {
        try {
            val user = call.receive<User>()
            val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())
            
            transaction {
                Users.insert {
                    it[username] = user.username
                    it[password] = hashedPassword
                }
            }
            
            call.respond(HttpStatusCode.Created)
        } catch (e: Exception) {
            println("Registration error: ${e.message}")
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
        }
    }

    post("/login") {
        try {
            val user = call.receive<User>()
            
            val dbUser = transaction {
                Users.findByUsername(user.username)
            }
            
            if (dbUser != null && BCrypt.checkpw(user.password, dbUser.password)) {
                val token = TokenManager.generateToken(user.username)
                call.respond(hashMapOf("token" to token))
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        } catch (e: Exception) {
            println("Login error: ${e.message}")
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error")
        }
    }

    authenticate {
        get("/auth") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal?.payload?.getClaim("username")?.asString()
            if (username != null) {
                call.respond(hashMapOf("username" to username))
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}
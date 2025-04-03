package utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object TokenManager {
    private const val SECRET = "your-secret-key" // В реальном приложении храните в защищенном месте
    private const val ISSUER = "text-analysis-server"
    private const val VALIDITY_IN_MS = 36_000_00 * 10 // 10 hours
    
    private val algorithm = Algorithm.HMAC256(SECRET)
    
    val verifier = JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .build()
        
    fun generateToken(username: String): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(ISSUER)
        .withClaim("username", username)
        .withExpiresAt(getExpiration())
        .sign(algorithm)
        
    private fun getExpiration() = Date(System.currentTimeMillis() + VALIDITY_IN_MS)
}
package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.TextAnalysisRequest
import services.ServiceManager

fun Route.analysisRoutes() {
    post("/analyze") {
        val request = call.receive<TextAnalysisRequest>()
        val result = ServiceManager.textAnalysisService.analyzeText(request)
        call.respond(result)
    }
} 
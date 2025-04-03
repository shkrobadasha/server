package services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import models.TextAnalysisRequest
import models.TextAnalysisResponse

class TextAnalysisService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

//обновляем тут

    private val colabUrl = "https://e2aa-34-121-188-59.ngrok-free.app"

    suspend fun analyzeText(request: TextAnalysisRequest): TextAnalysisResponse {
        return try {
            println("Sending request to: $colabUrl")
            println("Request body: $request")
            
            val response = client.post(colabUrl) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            println("Response received: ${response.status}")
            response.body<TextAnalysisResponse>()
        } catch (e: Exception) {
            println("Error analyzing text: ${e.message}")
            println("Error type: ${e.javaClass.simpleName}")
            e.printStackTrace()
            TextAnalysisResponse(false, 0.0)
        }
    }

    fun close() {
        client.close()
    }
} 
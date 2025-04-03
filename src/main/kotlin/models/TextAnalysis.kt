package models

import kotlinx.serialization.Serializable

@Serializable
data class TextAnalysisRequest(
    val text: String
)

@Serializable
data class TextAnalysisResponse(
    val isGenerated: Boolean,
    val confidence: Double
) 
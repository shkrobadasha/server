package services

object ServiceManager {
    val textAnalysisService = TextAnalysisService()

    fun shutdown() {
        textAnalysisService.close()
    }
} 
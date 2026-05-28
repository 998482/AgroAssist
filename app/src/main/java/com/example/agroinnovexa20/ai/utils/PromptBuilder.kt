package com.example.agroinnovexa20.ai.utils



object PromptBuilder {

    fun buildAdvisoryPrompt(
        crop: String,
        temperature: String,
        humidity: String,
        offlineAdvice: String
    ): String {

        return """
            You are an agriculture expert.

            Crop: $crop
            Temperature: $temperature
            Humidity: $humidity

            Existing Advice:
            $offlineAdvice

            Improve this advice for Indian farmers.
            Keep it practical and under 80 words.
        """.trimIndent()
    }
}
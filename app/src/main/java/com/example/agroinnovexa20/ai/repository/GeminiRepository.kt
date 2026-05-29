package com.example.agroinnovexa20.ai.repository


import com.example.agroinnovexa20.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel

class GeminiRepository {

    private val model =
        GenerativeModel(
            modelName = "models/gemini-2.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )

    suspend fun askGemini(
        prompt: String
    ): String {

        return try {

            val response =
                model.generateContent(prompt)

            response.text
                ?: "No response"

        } catch (e: Exception) {

            e.printStackTrace()

            "Error: ${e.message}"
        }
    }
}
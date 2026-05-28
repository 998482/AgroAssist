package com.example.agroinnovexa20.ai.repository

import com.google.ai.client.generativeai.GenerativeModel

class GeminiRepository {

    private val model =
        GenerativeModel(
            modelName = "models/gemini-2.5-flash",
            apiKey = "AIzaSyBtTw7Mf_83LOxdIIsj2S8Y6VhqDUmjxDo"
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
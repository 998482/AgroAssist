package com.example.agroinnovexa20.data.util

import com.example.agroinnovexa20.BuildConfig



import com.example.agroinnovexa20.domain.aimodel.CropAdvisoryResult
import com.google.ai.client.generativeai.GenerativeModel
import org.json.JSONObject

class GeminiRepository {

    private val model = GenerativeModel(
        modelName = "models/gemini-2.5-flash",
        apiKey =  BuildConfig.GEMINI_API_KEY
    )

    suspend fun getAdvisory(
        cropName: String,   // ← String, CropType nahi
        temp: Double,
        humidity: Int,
        language: String = "hi"
    ): CropAdvisoryResult {
        return try {
            val prompt = if (language == "hi") {
                """
            Aap ek anubhavi krishi visheshagya hain.
            Fasal: $cropName | Taapman: ${temp}°C | Nami: $humidity%
            
            SIRF yeh JSON do, kuch aur mat likho:
            {"crop":"$cropName","message":"aaj kya karna chahiye (2-3 seedhi Hindi waakyein)","reason":"kyun karna chahiye (1-2 waakyein)","riskLevel":"kam ya madhyam ya zyada","tip":"ek chhoti practical tip","emoji":"ek relevant emoji"}
            """.trimIndent()
            } else {
                """
            You are an agriculture expert for Indian farmers.
            Crop: $cropName | Temp: ${temp}°C | Humidity: $humidity%
            
            Reply ONLY with this JSON, nothing else:
            {"crop":"$cropName","message":"2-3 simple sentences on what to do today","reason":"1-2 sentences why","riskLevel":"low or medium or high","tip":"one practical tip","emoji":"one relevant emoji"}
            """.trimIndent()
            }

            val response = model.generateContent(prompt)
            val raw = response.text ?: throw Exception("Empty response")

            val clean = raw
                .replace("```json", "")
                .replace("```", "")
                .trim()

            val json = JSONObject(clean)

            CropAdvisoryResult(
                crop      = json.optString("crop", cropName),
                message   = json.optString("message", ""),
                reason    = json.optString("reason", ""),
                riskLevel = json.optString("riskLevel", "madhyam"),
                tip       = json.optString("tip", ""),
                emoji     = json.optString("emoji", "🌱")
            )

        } catch (e: Exception) {
            // Offline fallback — generic response
            CropAdvisoryResult(
                crop      = cropName,
                message   = "Abhi internet nahi hai. Baad mein try karein.",
                reason    = "AI se connect nahi ho paya.",
                riskLevel = "madhyam",
                tip       = "Fasal ki nigrani karte rahein.",
                emoji     = "🌾"
            )
        }
    }
}
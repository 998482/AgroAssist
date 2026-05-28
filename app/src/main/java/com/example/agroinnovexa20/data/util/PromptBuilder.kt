package com.example.agroinnovexa20.data.util


import com.example.agroinnovexa20.data.model.advisory.CropType

object PromptBuilder {

    fun buildAdvisoryPrompt(
        crop: CropType,
        temp: Double,
        humidity: Int,
        language: String = "hi" // "hi" = Hindi, "en" = English
    ): String {
        val cropName = crop.name.lowercase()
            .replaceFirstChar { it.uppercase() }

        return if (language == "hi") {
            """
            Aap ek anubhavi krishi visheshagya hain jo Indian kisan ko salah dete hain.
            
            Fasal: $cropName
            Taapman: ${temp}°C
            Aadrata (Humidity): $humidity%
            
            Neeche diye format mein SIRF JSON dena, kuch aur mat likhna:
            {
              "crop": "$cropName",
              "message": "kisan ko aaj kya karna chahiye (2-3 simple waakyein Hindi mein)",
              "reason": "kyun yeh karna chahiye (1-2 waakyein Hindi mein)",
              "riskLevel": "kam / madhyam / zyada",
              "tip": "ek chhoti practical tip Hindi mein",
              "emoji": "ek relevant emoji"
            }
            
            Language: Bilkul seedhi aur saral Hindi. Kisan padh sake aur samajh sake.
            """.trimIndent()
        } else {
            """
            You are an experienced agriculture expert advising Indian farmers.
            
            Crop: $cropName
            Temperature: ${temp}°C
            Humidity: $humidity%
            
            Reply ONLY with this JSON, nothing else:
            {
              "crop": "$cropName",
              "message": "what the farmer should do today (2-3 simple sentences)",
              "reason": "why they should do this (1-2 sentences)",
              "riskLevel": "low / medium / high",
              "tip": "one small practical tip",
              "emoji": "one relevant emoji"
            }
            """.trimIndent()
        }
    }
}
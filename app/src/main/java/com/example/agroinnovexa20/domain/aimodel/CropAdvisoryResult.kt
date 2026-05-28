package com.example.agroinnovexa20.domain.aimodel




data class CropAdvisoryResult(
    val crop: String,
    val message: String,
    val reason: String,
    val riskLevel: String,
    val tip: String = "",        // ← yeh add karo
    val emoji: String = "🌱"    // ← yeh add karo
)
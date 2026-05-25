package com.example.agroinnovexa20.data.model.model3

fun wheatAdvisory(temp: Double, humidity: Int): CropAdvisoryResult {
    return when {
        humidity > 75 ->
            CropAdvisoryResult(
                crop = "गेहूं",
                riskLevel = "HIGH",
                message = "गेहूं में फंगल रोग का खतरा है।",
                reason = "अधिक नमी से रस्ट और स्मट रोग फैल सकते हैं। खेत में जल निकासी सुनिश्चित करें।"
            )

        temp > 30 ->
            CropAdvisoryResult(
                crop = "गेहूं",
                riskLevel = "MEDIUM",
                message = "उच्च तापमान से गेहूं पर प्रभाव पड़ सकता है।",
                reason = "अधिक गर्मी से दाने कमजोर हो सकते हैं। सिंचाई का समय सही रखें।"
            )

        else ->
            CropAdvisoryResult(
                crop = "गेहूं",
                riskLevel = "LOW",
                message = "गेहूं की फसल सुरक्षित है।",
                reason = "मौसम अनुकूल है। सामान्य कृषि क्रियाएं जारी रखें।"
            )
    }
}

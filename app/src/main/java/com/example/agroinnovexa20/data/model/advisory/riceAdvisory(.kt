package com.example.agroinnovexa20.data.model.advisory

fun riceAdvisory(temp: Double, humidity: Int): CropAdvisoryResult {
    return when {
        humidity > 85 ->
            CropAdvisoryResult(
                crop = "धान",
                riskLevel = "HIGH",
                message = "धान में रोग लगने की संभावना अधिक है।",
                reason = "अधिक नमी से ब्लास्ट और शीथ ब्लाइट रोग बढ़ सकते हैं। जल स्तर नियंत्रित रखें।"
            )

        temp < 20 ->
            CropAdvisoryResult(
                crop = "धान",
                riskLevel = "MEDIUM",
                message = "कम तापमान से फसल की वृद्धि धीमी हो सकती है।",
                reason = "ठंडे मौसम में पौधों की बढ़त प्रभावित होती है। निगरानी रखें।"
            )

        else ->
            CropAdvisoryResult(
                crop = "धान",
                riskLevel = "LOW",
                message = "धान की फसल सामान्य स्थिति में है।",
                reason = "तापमान और नमी संतुलित हैं। सामान्य देखभाल पर्याप्त है।"
            )
    }
}

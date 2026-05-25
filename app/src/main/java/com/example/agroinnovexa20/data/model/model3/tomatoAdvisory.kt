package com.example.agroinnovexa20.data.model.model3

fun tomatoAdvisory(temp: Double, humidity: Int): CropAdvisoryResult {
    return when {
        humidity > 80 && temp > 25 ->
            CropAdvisoryResult(
                crop = "टमाटर",
                riskLevel = "HIGH",
                message = "टमाटर में फंगल रोग का अधिक खतरा है।",
                reason = "अधिक नमी और गर्म तापमान फंगल संक्रमण के लिए अनुकूल हैं। सिंचाई रोकें और हवा का प्रवाह बनाए रखें।"
            )

        humidity in 60..80 ->
            CropAdvisoryResult(
                crop = "टमाटर",
                riskLevel = "MEDIUM",
                message = "टमाटर की फसल पर मध्यम जोखिम है।",
                reason = "नमी मध्यम स्तर पर है। पत्तियों की नियमित जांच करें और अधिक पानी न दें।"
            )

        else ->
            CropAdvisoryResult(
                crop = "टमाटर",
                riskLevel = "LOW",
                message = "टमाटर की फसल सुरक्षित है।",
                reason = "मौसम अनुकूल है। सामान्य देखभाल जारी रखें।"
            )
    }
}

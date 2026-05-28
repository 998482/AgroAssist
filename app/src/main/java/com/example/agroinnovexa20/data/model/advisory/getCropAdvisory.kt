package com.example.agroinnovexa20.data.model.advisory

fun getCrop(
    temp: Double,
    humidity: Int
): CropAdvisoryResult {

    return when {

        humidity > 80 && temp > 25 -> CropAdvisoryResult(
            crop = "Tomato",
            riskLevel = "HIGH",
            message = "High risk of fungal disease detected. Immediate preventive action is required to protect the crop.",
            reason = "Humidity above 80% combined with warm temperatures creates ideal conditions for fungal infections like blight and leaf mold. Avoid irrigation today, ensure proper ventilation, and consider preventive fungicide spray."
        )

        humidity in 60..80 -> CropAdvisoryResult(
            crop = "Tomato",
            riskLevel = "MEDIUM",
            message = "Moderate disease risk. Crop needs careful monitoring over the next few days.",
            reason = "Moderately high humidity can slowly promote pest and disease development. Reduce excess watering, inspect leaves regularly, and maintain proper spacing between plants to improve airflow."
        )

        else -> CropAdvisoryResult(
            crop = "Tomato",
            riskLevel = "LOW",
            message = "Crop conditions are favorable and stable at the moment.",
            reason = "Low humidity and balanced temperature reduce disease chances. Continue regular irrigation and basic crop care practices, but stay alert to sudden weather changes."
        )
    }
}


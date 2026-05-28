package com.example.agroinnovexa20.data.model.advisory

fun getCropAdvisory(
    crop: CropType,
    temp: Double,
    humidity: Int
): CropAdvisoryResult {

    return when (crop) {

        CropType.TOMATO -> tomatoAdvisory(temp, humidity)

        CropType.WHEAT -> wheatAdvisory(temp, humidity)

        CropType.RICE -> riceAdvisory(temp, humidity)
    }
}

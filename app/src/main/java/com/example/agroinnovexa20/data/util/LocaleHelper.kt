package com.example.agroinnovexa20.ui.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

fun getLocalString(context: Context, locale: String, resId: Int): String {
    val config = Configuration(context.resources.configuration)
    val newLocale = Locale(locale)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        config.setLocale(newLocale)
    } else {
        @Suppress("DEPRECATION")
        config.locale = newLocale
    }
    return context.createConfigurationContext(config).resources.getString(resId)
}
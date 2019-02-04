package com.dertyp7214.preferencesplus.core

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt

fun Int.dp(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

@ColorInt
fun Int.saturate(value: Float): Int {
    val hsv = floatArrayOf(0F, 0F, 0F)
    Color.colorToHSV(this, hsv)
    hsv[1] = value
    hsv[2] = value
    return Color.HSVToColor(hsv)
}
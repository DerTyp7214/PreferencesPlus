package com.dertyp7214.preferencesplus.core

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity

fun Context.nextActivity(): AppCompatActivity? {
    return when (this) {
        null -> null
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.nextActivity()
        else -> null
    }
}
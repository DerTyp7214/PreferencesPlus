package com.dertyp7214.preferencesplus.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet

import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatSeekBar

/**
 * Created by lengw on 20.09.2017.
 */

class ColorSeekBar(context: Context, attrs: AttributeSet) : AppCompatSeekBar(context, attrs) {

    init {
        val black = ColorStateList(arrayOf(intArrayOf()), intArrayOf(Color.BLACK))

        thumbTintList = black
        progressTintList = black
    }

    fun setColor(@ColorInt c: Int) {
        val color = ColorStateList(arrayOf(intArrayOf()), intArrayOf(c))

        thumbTintList = color
        progressTintList = color
    }

    var defaultColor: Int = Color.BLACK
        set(value) {
            setColor(value)
            field = value
        }
}
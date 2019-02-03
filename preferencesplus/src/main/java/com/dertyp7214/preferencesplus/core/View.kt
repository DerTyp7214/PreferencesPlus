package com.dertyp7214.preferencesplus.core

import android.view.View
import android.view.ViewGroup

fun View.setMargins(l: Int, t: Int, r: Int, b: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(l, t, r, b)
        requestLayout()
    }
}

fun View.setWidth(width: Int) {
    if (layoutParams != null) layoutParams.width = width
    else layoutParams = ViewGroup.LayoutParams(width, height)
}

fun View.setHeight(height: Int) {
    if (layoutParams != null) layoutParams.height = height
    else layoutParams = ViewGroup.LayoutParams(width, height)
}
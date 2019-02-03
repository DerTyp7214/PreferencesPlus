package com.dertyp7214.preferencesplus.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

class ColorUtil {
    companion object {

        fun getColor(hexCode: String): Int {
            return getIntFromColor(Color.red(Color.parseColor(hexCode)).toFloat(), Color.green(Color.parseColor(hexCode)).toFloat(), Color.blue(Color.parseColor(hexCode)).toFloat())
        }

        private fun getIntFromColor(Red: Float, Green: Float, Blue: Float): Int {
            var r = Math.round(255.0f * (256.0f - Red))
            var g = Math.round(255.0f * (256.0f - Green))
            var b = Math.round(255.0f * (256.0f - Blue))
            r = r shl 16 and 16711680
            g = g shl 8 and '\uff00'.toInt()
            b = b and 255
            return -16777216 or r or g or b
        }

        fun getDominantColor(bitmap: Bitmap): Int {
            val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
            val color = newBitmap.getPixel(0, 0)
            newBitmap.recycle()
            return color
        }

        fun drawableToBitmap(drawable: Drawable): Bitmap {
            val bitmap: Bitmap? = if (drawable.intrinsicWidth > 0 && drawable.intrinsicHeight > 0) {
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            } else {
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            }
            if (drawable is BitmapDrawable) {
                if (drawable.bitmap != null) {
                    return drawable.bitmap
                }
            }

            val canvas = Canvas(bitmap!!)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        fun calculateColor(color1: Int, color2: Int, max: Int, current: Int): Int {
            val strC1 = Integer.toHexString(color1)
            val strC2 = Integer.toHexString(color2)
            var retColor = "#"

            var i = 2
            while (i < strC1.length) {
                val tmp1 = strC1[i] + "" + strC1[i + 1]
                val tmp2 = strC2[i] + "" + strC2[i + 1]
                val tmp1Color = java.lang.Long.parseLong(tmp1, 16).toInt()
                val tmp2Color = java.lang.Long.parseLong(tmp2, 16).toInt()
                val dif = tmp2Color - tmp1Color
                val difCalc = dif.toDouble() / max.toDouble()
                var colorMerge = (difCalc * current.toDouble()).toInt()
                if (tmp1Color + colorMerge > 255) {
                    colorMerge = 0
                }

                var add = Integer.toHexString(tmp1Color + colorMerge)
                if (add.length < 2) {
                    add = "0$add"
                }

                retColor += add
                i += 2
            }

            return Color.parseColor(retColor)
        }
    }
}
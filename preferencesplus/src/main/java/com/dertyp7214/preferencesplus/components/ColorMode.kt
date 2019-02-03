package com.dertyp7214.preferencesplus.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.google.android.material.bottomsheet.BottomSheetDialog

enum class ColorMode {
    RGB {
        val RED = 1
        val GREEN = 2
        val BLUE = 3
        override val length: Int
            get() = 7
        override val sub: Int
            get() = 2
        override lateinit var text: BottomSheetText

        override fun setText(text: Int, @ColorInt color: Int, type: Int) {
            this.text.setText(((text.toFloat() / 360) * 255).toInt().toString(), color)
        }

        override fun calcColor(bars: List<Int>): Int {
            if (bars.size < 3) throw IncorrectColor("RGB needs 3 values")
            return if (bars.size == 4) Color.argb(calcValue(255, bars[0]).toInt(),
                    calcValue(255, bars[1]).toInt(),
                    calcValue(255, bars[2]).toInt(),
                    calcValue(255, bars[3]).toInt())
            else Color.rgb(calcValue(255, bars[0]).toInt(),
                    calcValue(255, bars[1]).toInt(),
                    calcValue(255, bars[2]).toInt())
        }
    },
    ARGB {
        val ALPHA = 0
        val RED = 1
        val GREEN = 2
        val BLUE = 3
        override val length: Int
            get() = 9
        override val sub: Int
            get() = 0
        override lateinit var text: BottomSheetText

        override fun setText(text: Int, @ColorInt color: Int, type: Int) {
            this.text.setText(((text.toFloat() / 360) * 255).toInt().toString(), color)
        }

        override fun calcColor(bars: List<Int>): Int {
            if (bars.size < 4) throw IncorrectColor("ARGB needs 4 values")
            return Color.argb(calcValue(255, bars[0]).toInt(),
                    calcValue(255, bars[1]).toInt(),
                    calcValue(255, bars[2]).toInt(),
                    calcValue(255, bars[3]).toInt())
        }
    },
    HSV {
        val HUE = 1
        val SATURATION = 2
        val VALUE = 3
        override val length: Int
            get() = 7
        override val sub: Int
            get() = 2
        override lateinit var text: BottomSheetText

        override fun setText(text: Int, @ColorInt color: Int, type: Int) {
            this.text.setText(((text.toFloat() / 360) * when (type) {
                HUE -> 360
                else -> 1
            }).toInt().toString(), color)
        }

        override fun calcColor(bars: List<Int>): Int {
            if (bars.size < 3) throw IncorrectColor("HSV needs 3 values")
            val hsv = floatArrayOf(calcValue(360, bars[0]),
                    calcValue(1, bars[1]),
                    calcValue(1, bars[2]))
            return Color.HSVToColor(hsv)
        }
    };

    abstract val length: Int
    abstract val sub: Int
    abstract var text: BottomSheetText
    abstract fun calcColor(bars: List<Int>): Int
    abstract fun setText(text: Int, color: Int, type: Int)
    fun calcValue(amount: Int, value: Int): Float {
        return amount * (value.toFloat() / 360)
    }

    @SuppressLint("ValidFragment")
    class BottomSheetText internal constructor(context: Context) : BottomSheetDialog(context) {
        private lateinit var text: String
        private var textView: TextView = TextView(context)

        init {
            textView.gravity = Gravity.CENTER_HORIZONTAL
            textView.textSize = 18f
            setContentView(textView)
            setCancelable(false)
            window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        internal fun setText(text: String, @ColorInt color: Int) {
            this.text = text
            textView.text = text
            textView.setBackgroundColor(color)
            textView.setTextColor(
                    if (ColorUtils.calculateLuminance(color) < 0.5) Color.WHITE else Color.BLACK)
        }
    }
}

class IncorrectColor(message: String?) : Exception(message)
package com.dertyp7214.preferencesplus.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.dertyp7214.preferencesplus.core.saturate
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

        override fun float(id: Int): Boolean {
            return false
        }

        private var bars: ArrayList<ColorSeekBar> = ArrayList()
        private var darkMode = false

        override fun barColor(bars: List<ColorSeekBar>, darkMode: Boolean) {
            this.bars.clear()
            this.bars.addAll(bars)
            this.darkMode = darkMode
        }

        override fun setText(text: Int, @ColorInt color: Int, type: Int) {
            val value = ((text.toFloat() / 360) * 255).toInt().toString()
            this.text.setText(value, color)
        }

        override fun calcColor(bars: List<Int>): Int {
            if (bars.size < 3) throw IncorrectColor("RGB needs 3 values")
            return (if (bars.size == 4) Color.argb(calcValue(255F, bars[0]).toInt(),
                    calcValue(255F, bars[1]).toInt(),
                    calcValue(255F, bars[2]).toInt(),
                    calcValue(255F, bars[3]).toInt())
            else Color.rgb(calcValue(255F, bars[0]).toInt(),
                    calcValue(255F, bars[1]).toInt(),
                    calcValue(255F, bars[2]).toInt())).apply {
                if (this@RGB.bars.size == 4) {
                    this@RGB.bars[1].setColor(ColorUtils.blendARGB(
                            if (darkMode) Color.WHITE else Color.BLACK,
                            Color.RED, this@RGB.bars[1].progress.toFloat() / this@RGB.bars[1].max.toFloat()))
                    this@RGB.bars[2].setColor(ColorUtils.blendARGB(
                            if (darkMode) Color.WHITE else Color.BLACK,
                            Color.GREEN, this@RGB.bars[2].progress.toFloat() / this@RGB.bars[2].max.toFloat()))
                    this@RGB.bars[3].setColor(ColorUtils.blendARGB(
                            if (darkMode) Color.WHITE else Color.BLACK,
                            Color.BLUE, this@RGB.bars[3].progress.toFloat() / this@RGB.bars[3].max.toFloat()))
                }
            }
        }

        override fun getValues(color: Int): List<Pair<Float, Float>> {
            return arrayListOf(
                    Pair(1F, 1F),
                    Pair(Color.red(color).toFloat(), 255F),
                    Pair(Color.green(color).toFloat(), 255F),
                    Pair(Color.blue(color).toFloat(), 255F)
            )
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

        override fun float(id: Int): Boolean {
            return false
        }

        private var bars: ArrayList<ColorSeekBar> = ArrayList()
        private var darkMode = false

        override fun barColor(bars: List<ColorSeekBar>, darkMode: Boolean) {
            this.bars.clear()
            this.bars.addAll(bars)
            this.darkMode = darkMode
        }

        override fun setText(text: Int, @ColorInt color: Int, type: Int) {
            val value = ((text.toFloat() / 360) * 255).toInt().toString()
            this.text.setText(value, color)
        }

        override fun calcColor(bars: List<Int>): Int {
            if (bars.size < 4) throw IncorrectColor("ARGB needs 4 values")
            return Color.argb(calcValue(255F, bars[0]).toInt(),
                    calcValue(255F, bars[1]).toInt(),
                    calcValue(255F, bars[2]).toInt(),
                    calcValue(255F, bars[3]).toInt()).apply {
                if (this@ARGB.bars.size == 4) {
                    this@ARGB.bars[0].setColor(if (darkMode) Color.WHITE else Color.BLACK)
                    this@ARGB.bars[1].setColor(ColorUtils.blendARGB(
                            if (darkMode) Color.WHITE else Color.BLACK,
                            Color.RED, this@ARGB.bars[1].progress.toFloat() / this@ARGB.bars[1].max.toFloat()))
                    this@ARGB.bars[2].setColor(ColorUtils.blendARGB(
                            if (darkMode) Color.WHITE else Color.BLACK,
                            Color.GREEN, this@ARGB.bars[2].progress.toFloat() / this@ARGB.bars[2].max.toFloat()))
                    this@ARGB.bars[3].setColor(ColorUtils.blendARGB(
                            if (darkMode) Color.WHITE else Color.BLACK,
                            Color.BLUE, this@ARGB.bars[3].progress.toFloat() / this@ARGB.bars[3].max.toFloat()))
                }
            }
        }

        override fun getValues(color: Int): List<Pair<Float, Float>> {
            return arrayListOf(
                    Pair(Color.alpha(color).toFloat(), 255F),
                    Pair(Color.red(color).toFloat(), 255F),
                    Pair(Color.green(color).toFloat(), 255F),
                    Pair(Color.blue(color).toFloat(), 255F)
            )
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

        override fun float(id: Int): Boolean {
            return id != HUE
        }

        private var bars: ArrayList<ColorSeekBar> = ArrayList()
        private var darkMode = false

        override fun barColor(bars: List<ColorSeekBar>, darkMode: Boolean) {
            this.bars.clear()
            this.bars.addAll(bars)
            this.darkMode = darkMode
        }

        override fun setText(text: Int, @ColorInt color: Int, type: Int) {
            val float: Float = ((text.toFloat() / 360) * when (type) {
                HUE -> 360
                else -> 1
            })
            val value = if (type == HUE) {
                float.toInt().toString()
            } else {
                ((float * 10F).toInt() / 10F).toString()
            }
            this.text.setText(value, color)
        }

        override fun calcColor(bars: List<Int>): Int {
            if (bars.size < 3) throw IncorrectColor("HSV needs 3 values")
            val hsv = floatArrayOf(calcValue(360F, bars[1]),
                    calcValue(1F, bars[2]),
                    calcValue(1F, bars[3]))
            return Color.HSVToColor(hsv).apply {
                Log.d("BARS", this@HSV.bars.size.toString())
                if (this@HSV.bars.size == 4) {
                    this@HSV.bars[1].setColor(this.saturate(1F))
                    this@HSV.bars[2].setColor(this.saturate(1F))
                    this@HSV.bars[3].setColor(this.saturate(1F))
                }
            }
        }

        override fun getValues(color: Int): List<Pair<Float, Float>> {
            val hsv = floatArrayOf(0F, 0F, 0F)
            Color.colorToHSV(color, hsv)
            return arrayListOf(
                    Pair(1F, 1F),
                    Pair(hsv[0], 360F),
                    Pair(hsv[1], 1F),
                    Pair(hsv[2], 1F)
            )
        }
    },
    HSL {
        private val HUE = 1
        override val length: Int
            get() = 7
        override val sub: Int
            get() = 2
        override lateinit var text: BottomSheetText

        override fun float(id: Int): Boolean {
            return id != HUE
        }

        private var bars: ArrayList<ColorSeekBar> = ArrayList()
        private var darkMode = false

        override fun barColor(bars: List<ColorSeekBar>, darkMode: Boolean) {
            this.bars.clear()
            this.bars.addAll(bars)
            this.darkMode = darkMode
        }

        override fun calcColor(bars: List<Int>): Int {
            return ColorUtils.HSLToColor(floatArrayOf(
                    calcValue(360F, bars[1]),
                    calcValue(1F, bars[2]),
                    calcValue(1F, bars[3])
            )).apply {
                if (this@HSL.bars.size == 4) {
                    this@HSL.bars[1].setColor(this.saturate(1F))
                    this@HSL.bars[2].setColor(this.saturate(1F))
                    this@HSL.bars[3].setColor(this.saturate(1F))
                }
            }
        }

        override fun setText(text: Int, color: Int, type: Int) {
            val float: Float = ((text.toFloat() / 360) * when (type) {
                HUE -> 360
                else -> 1
            })
            val value = if (type == HUE) {
                float.toInt().toString()
            } else {
                ((float * 10F).toInt() / 10F).toString()
            }
            this.text.setText(value, color)
        }

        override fun getValues(color: Int): List<Pair<Float, Float>> {
            val hsl = floatArrayOf(0F, 0F, 0F)
            ColorUtils.colorToHSL(color, hsl)
            return arrayListOf(
                    Pair(1F, 1F),
                    Pair(hsl[0], 360F),
                    Pair(hsl[1], 1F),
                    Pair(hsl[2], 1F)
            )
        }
    },
    LAB { // TODO: not working properly (text is not right, and values are a bit off)
        private val LIGHTNESS = 1
        override val length: Int
            get() = 7
        override val sub: Int
            get() = 2
        override lateinit var text: BottomSheetText

        override fun float(id: Int): Boolean {
            return false
        }

        private var bars: ArrayList<ColorSeekBar> = ArrayList()
        private var darkMode = false

        override fun barColor(bars: List<ColorSeekBar>, darkMode: Boolean) {
            this.bars.clear()
            this.bars.addAll(bars)
            this.darkMode = darkMode
        }

        override fun calcColor(bars: List<Int>): Int {
            return ColorUtils.LABToColor(
                    calcValue(100F, bars[1]).toDouble(),
                    (calcValue(255F, bars[2]) - 128).toDouble(),
                    (calcValue(255F, bars[3]) - 128).toDouble()
            ).apply {
                if (this@LAB.bars.size == 4) {
                    this@LAB.bars[1].setColor(ColorUtils.blendARGB(if (darkMode) Color.WHITE else Color.BLACK,
                            this.saturate(1F), this@LAB.bars[1].progress.toFloat() / this@LAB.bars[1].max.toFloat()))
                    this@LAB.bars[2].setColor(ColorUtils.blendARGB(Color.GREEN, Color.RED,
                            this@LAB.bars[2].progress.toFloat() / this@LAB.bars[2].max.toFloat()))
                    this@LAB.bars[3].setColor(ColorUtils.blendARGB(Color.BLUE, Color.YELLOW,
                            this@LAB.bars[3].progress.toFloat() / this@LAB.bars[3].max.toFloat()))
                }
            }
        }

        override fun setText(text: Int, color: Int, type: Int) {
            val value = if (LIGHTNESS != type) 128 else 0
            this.text.setText((((text.toFloat() / 360) * when (type) {
                LIGHTNESS -> 100
                else -> 255
            }).toInt() - value).toString(), color)
        }

        override fun getValues(color: Int): List<Pair<Float, Float>> {
            val lab = doubleArrayOf(0.0, 0.0, 0.0)
            ColorUtils.colorToLAB(color, lab)
            return arrayListOf(
                    Pair(1F, 1F),
                    Pair(lab[0].toFloat(), 100F),
                    Pair(lab[1].toFloat() + 128, 255F),
                    Pair(lab[2].toFloat() + 128, 255F)
            )
        }
    },
    XYZ {
        override val length: Int
            get() = 7
        override val sub: Int
            get() = 2
        override lateinit var text: BottomSheetText

        override fun float(id: Int): Boolean {
            return false
        }

        private var bars: ArrayList<ColorSeekBar> = ArrayList()
        private var darkMode = false

        override fun barColor(bars: List<ColorSeekBar>, darkMode: Boolean) {
            this.bars.clear()
            this.bars.addAll(bars)
            this.darkMode = darkMode
        }

        override fun calcColor(bars: List<Int>): Int {
            return ColorUtils.XYZToColor(
                    calcValue(95.047F, bars[1]).toDouble(),
                    calcValue(100F, bars[2]).toDouble(),
                    calcValue(108.883F, bars[3]).toDouble()
            ).apply {
                if (this@XYZ.bars.size == 4) {
                    val colorX = ColorUtils.XYZToColor(95.047, 0.0, 0.0)
                    val colorY = ColorUtils.XYZToColor(0.0, 100.0, 0.0)
                    val colorZ = ColorUtils.XYZToColor(0.0, 0.0, 108.883)
                    this@XYZ.bars[1].setColor(colorX)
                    this@XYZ.bars[2].setColor(colorY)
                    this@XYZ.bars[3].setColor(colorZ)
                }
            }
        }

        override fun setText(text: Int, color: Int, type: Int) {
            this.text.setText(((text.toFloat() / 360) * when (type) {
                1 -> 95.047F
                3 -> 108.883F
                else -> 100F
            }).toInt().toString(), color)
        }

        override fun getValues(color: Int): List<Pair<Float, Float>> {
            val lab = doubleArrayOf(0.0, 0.0, 0.0)
            ColorUtils.colorToXYZ(color, lab)
            return arrayListOf(
                    Pair(1F, 1F),
                    Pair(lab[0].toFloat(), 95.047F),
                    Pair(lab[1].toFloat(), 100F),
                    Pair(lab[2].toFloat(), 108.883F)
            )
        }
    },
    CMYK {
        override val length: Int
            get() = 7
        override val sub: Int
            get() = 2
        override lateinit var text: BottomSheetText

        override fun float(id: Int): Boolean {
            return false
        }

        private var bars: ArrayList<ColorSeekBar> = ArrayList()
        private var darkMode = false

        override fun barColor(bars: List<ColorSeekBar>, darkMode: Boolean) {
            this.bars.clear()
            this.bars.addAll(bars)
            this.darkMode = darkMode
        }

        override fun calcColor(bars: List<Int>): Int {
            val c = calcValue(255F, bars[1])
            val m = calcValue(255F, bars[2])
            val y = calcValue(255F, bars[3])
            val k = calcValue(255F, bars[0])
            val r = ((255 - c) * (255 - k)) / 255
            val g = ((255 - m) * (255 - k)) / 255
            val b = ((255 - y) * (255 - k)) / 255
            return Color.rgb(r.toInt(), g.toInt(), b.toInt()).apply {
                if (this@CMYK.bars.size == 4) {
                    this@CMYK.bars[0].setColor(if (darkMode) Color.WHITE else Color.BLACK)
                    this@CMYK.bars[1].setColor(ColorUtils.blendARGB(
                            if (darkMode) Color.WHITE else Color.BLACK,
                            Color.CYAN, this@CMYK.bars[1].progress.toFloat() / this@CMYK.bars[1].max.toFloat()))
                    this@CMYK.bars[2].setColor(ColorUtils.blendARGB(
                            if (darkMode) Color.WHITE else Color.BLACK,
                            Color.MAGENTA, this@CMYK.bars[2].progress.toFloat() / this@CMYK.bars[2].max.toFloat()))
                    this@CMYK.bars[3].setColor(ColorUtils.blendARGB(
                            if (darkMode) Color.WHITE else Color.BLACK,
                            Color.YELLOW, this@CMYK.bars[3].progress.toFloat() / this@CMYK.bars[3].max.toFloat()))
                }
            }
        }

        override fun setText(text: Int, color: Int, type: Int) {
            val value = ((text.toFloat() / 360) * 100).toInt().toString()
            this.text.setText(value, color)
        }

        override fun getValues(color: Int): List<Pair<Float, Float>> {
            val red = 0xff0000 and color shr 16
            val green = 0xff00 and color shr 8
            val blue = 0xff and color
            val black = Math.min(1.0f - red / 255.0f,
                    Math.min(1.0f - green / 255.0f, 1.0f - blue / 255.0f))
            var cyan = 1.0f
            var magenta = 1.0f
            var yellow = 1.0f
            if (black != 1.0f) {
                cyan = (1.0f - red / 255.0f - black) / (1.0f - black)
                magenta = (1.0f - green / 255.0f - black) / (1.0f - black)
                yellow = (1.0f - blue / 255.0f - black) / (1.0f - black)
            }
            return arrayListOf(
                    Pair(black * 100F, 100F),
                    Pair(cyan * 100F, 100F),
                    Pair(magenta * 100F, 100F),
                    Pair(yellow * 100F, 100F)
            )
        }
    };

    abstract val length: Int
    abstract val sub: Int
    abstract var text: BottomSheetText
    abstract fun float(id: Int): Boolean
    abstract fun calcColor(bars: List<Int>): Int
    abstract fun setText(text: Int, color: Int, type: Int)
    abstract fun getValues(color: Int): List<Pair<Float, Float>>
    abstract fun barColor(bars: List<ColorSeekBar>, darkMode: Boolean = false)
    fun calcValue(amount: Float, value: Int): Float {
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
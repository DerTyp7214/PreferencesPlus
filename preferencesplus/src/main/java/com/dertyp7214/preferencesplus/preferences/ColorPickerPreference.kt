package com.dertyp7214.preferencesplus.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.View
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.edit
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceViewHolder
import com.dertyp7214.preferencesplus.R
import com.dertyp7214.preferencesplus.components.ColorMode
import com.dertyp7214.preferencesplus.components.ColorPicker
import com.dertyp7214.preferencesplus.core.dp

open class ColorPickerPreference : Preference {

    private var colorView: View? = null
    private var showHex = false
    private var alphaWhileSelecting = .3F

    @SuppressLint("NewApi")
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
            context,
            attrs,
            defStyleAttr,
            defStyleRes
    ) {
        this.init(attrs)
    }

    @SuppressLint("NewApi")
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.init(attrs)
    }

    constructor(context: Context) : super(context) {
        this.init()
    }

    @SuppressLint("Recycle")
    open fun init(attrs: AttributeSet? = null) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference)
            showHex = typedArray.getBoolean(R.styleable.ColorPickerPreference_showHex, false)
            alphaWhileSelecting = typedArray.getFloat(R.styleable.ColorPickerPreference_alphaWhileSelecting, .3F)
        }
    }

    var value: Int = Color.GRAY
        get() {
            return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, Color.GRAY)
        }
        set(value) {
            field = value
            PreferenceManager.getDefaultSharedPreferences(context).edit {
                putInt(key, value)
            }
            if (onPreferenceChangeListener != null)
                onPreferenceChangeListener.onPreferenceChange(this, value)
            notifyChanged()
        }

    override fun onClick() {
        ColorPicker(context).apply {
            colorMode = ColorMode.RGB
            setColor(value)
            setAnimationTime(200)
            onTouchListener(object : ColorPicker.TouchListener {
                override fun startTouch() {
                    this@apply.toast(true)
                    this@apply.setAlpha(alphaWhileSelecting)
                    this@apply.disableInput()
                }

                override fun stopTouch() {
                    this@apply.toast(false)
                    this@apply.setAlpha(1F)
                    this@apply.enableInput()
                }
            })
            setListener(object : ColorPicker.Listener {
                override fun color(color: Int) {
                    value = color
                }

                override fun update(color: Int) {}
                override fun cancel() {}
            })
            show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        if (holder != null) {
            val frame: LinearLayout = holder.findViewById(android.R.id.widget_frame) as LinearLayout
            val color = value
            colorView = View(context).apply {
                setBackgroundResource(R.drawable.circle)
                ((background as LayerDrawable).findDrawableByLayerId(R.id.plate_color) as GradientDrawable).setColor(color)
                if (layoutParams == null) {
                    layoutParams = LinearLayout.LayoutParams(30.dp(context), 30.dp(context))
                } else {
                    layoutParams.width = 30.dp(context)
                    layoutParams.height = 30.dp(context)
                }
                requestLayout()
            }
            frame.removeAllViews()
            frame.addView(colorView)
            frame.visibility = VISIBLE
            if (showHex) {
                val summaryTextView = holder.findViewById(android.R.id.summary) as TextView
                summaryTextView.text = "#${Integer.toHexString(value).substring(2).toUpperCase()}"
                summaryTextView.visibility = VISIBLE
            }
        }
    }
}
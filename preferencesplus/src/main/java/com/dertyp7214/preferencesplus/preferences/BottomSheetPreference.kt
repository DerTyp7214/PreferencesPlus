/*
 * Copyright (c) 2019.
 * Created by Josua Lengwenath
 */

package com.dertyp7214.preferencesplus.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.preference.ListPreference
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dertyp7214.preferencesplus.R
import com.dertyp7214.preferencesplus.core.dp
import com.dertyp7214.preferencesplus.core.nextActivity
import com.dertyp7214.preferencesplus.core.setMargins
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BottomSheetPreference : ListPreference {
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
        this.init(null)
    }

    var roundedCorners = false

    open fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetPreference)
            roundedCorners = typedArray.getBoolean(R.styleable.BottomSheetPreference_roundedCorners, false)
            typedArray.recycle()
        }
    }

    override fun onClick() {
        openBottomSheet()
    }

    private fun openBottomSheet() {
        val list = ArrayList<Pair<String, String>>()
        val entryList = ArrayList<String>()
        val entryValueList = ArrayList<String>()
        if (entries != null && entryValues != null && entries.isNotEmpty() && entryValues.isNotEmpty()) {
            entries.iterator().forEach { entryList.add(it.toString()) }
            entryValues.iterator().forEach { entryValueList.add(it.toString()) }
            if (entryList.size == entryValueList.size) {
                entryList.forEachIndexed { index, s ->
                    list.add(Pair(s, entryValueList[index]))
                }
            }
            BottomSheet(
                    list,
                    entryValueList.indexOf(value),
                    roundedCorners
            ) {
                value = it
                if (onPreferenceChangeListener != null) onPreferenceChangeListener.onPreferenceChange(this, it)
            }.show(context.nextActivity()!!.supportFragmentManager, "")
        } else {
            Toast.makeText(context, R.string.no_entries, Toast.LENGTH_LONG).show()
        }
    }

    class BottomSheet(
            private val list: ArrayList<Pair<String, String>>,
            private val selectedIndex: Int,
            private val roundedCorners: Boolean,
            private val clickListener: (element: String) -> Unit
    ) :
            BottomSheetDialogFragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return LinearLayout(context).apply {
                val backgroundColor = getAttrColor(context, android.R.attr.windowBackground)
                if (roundedCorners) {
                    setBackgroundResource(R.drawable.round_corners)
                    val drawable = background as GradientDrawable
                    drawable.setColor(backgroundColor)
                } else {
                    setBackgroundColor(backgroundColor)
                }
                orientation = LinearLayout.VERTICAL
                setPadding(5.dp(context))
                addView(RecyclerView(context!!).apply {
                    adapter = Adapter(context!!, list, selectedIndex) {
                        clickListener(it)
                        this@BottomSheet.dismiss()
                    }
                    layoutManager = LinearLayoutManager(context)
                    setPadding(0, 7.dp(context), 0, 0)
                })
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        }

        companion object {
            private fun getAttrColor(context: Context, attr: Int): Int {
                return try {
                    val ta = context.obtainStyledAttributes(intArrayOf(attr))
                    val colorAccent = ta.getColor(0, 0)
                    ta.recycle()
                    colorAccent
                } catch (e: Exception) {
                    Color.WHITE
                }
            }
        }

        private class Adapter(
                private val context: Context,
                private val list: ArrayList<Pair<String, String>>,
                private val selectedIndex: Int,
                private val clickListener: (element: String) -> Unit
        ) : RecyclerView.Adapter<Adapter.ViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val button = RadioButton(context)
                button.id = R.id.bottomSheetPreferenceItemId
                button.setPadding(8.dp(context))
                button.setTextSize(COMPLEX_UNIT_SP, 18F)
                return ViewHolder(button)
            }

            override fun getItemCount(): Int = list.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val pair = list[position]

                holder.button.text = pair.second
                holder.button.isChecked = position == selectedIndex
                holder.button.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                holder.button.setMargins(0, 0, 0, 7.dp(context))
                holder.button.setOnClickListener {
                    clickListener(pair.first)
                }
            }

            class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
                val button: RadioButton = v.findViewById(R.id.bottomSheetPreferenceItemId)
            }
        }
    }
}

package com.dertyp7214.preferencesplus.components

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText

class CustomEditText : EditText {

    private var pasteListener: (text: String) -> Unit = {}
    private var copyListener: (text: String) -> Unit = {}
    private var cutListener: (text: String) -> Unit = {}

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    fun setPasteListener(listener: (text: String) -> Unit) {
        this.pasteListener = listener
    }

    fun setCopyListener(listener: (text: String) -> Unit) {
        this.copyListener = listener
    }

    fun setCutListener(listener: (text: String) -> Unit) {
        this.cutListener = listener
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        val consumed = super.onTextContextMenuItem(id)
        when (id) {
            android.R.id.cut -> cutListener(this.text.toString())
            android.R.id.paste -> pasteListener(this.text.toString())
            android.R.id.copy -> copyListener(this.text.toString())
        }
        return consumed
    }
}
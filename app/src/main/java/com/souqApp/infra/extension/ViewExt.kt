package com.souqApp.infra.extension

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.souqApp.R

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun TextInputLayout.activeBorder(context: Context, active: Boolean) {
    this.boxStrokeColor =
        ContextCompat.getColor(context, if (active) R.color.green else R.color.red)
}

fun ActionBar.setup(
    homeAsUpEnabled: Boolean = true,
    showHomeEnabled: Boolean = true,
    showTitleEnabled: Boolean = false,
) {
    setDisplayHomeAsUpEnabled(true)
    setDisplayShowHomeEnabled(true)
    setDisplayShowTitleEnabled(false)
}


fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible)
        View.VISIBLE
    else
        View.GONE
}

fun ProgressBar.start(start: Boolean) {
    this.isVisible(start)
    if (start) {
        this.isIndeterminate = true
    } else {
        this.progress = 0
    }
}
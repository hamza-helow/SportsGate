package com.souqApp.infra.extension

import android.content.Context
import android.view.View
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

fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible)
        View.VISIBLE
    else
        View.GONE
}
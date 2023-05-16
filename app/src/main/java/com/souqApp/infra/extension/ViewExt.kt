package com.souqApp.infra.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.souqApp.R


fun TextInputLayout.activeBorder(context: Context, active: Boolean) {
    this.boxStrokeColor =
        ContextCompat.getColor(context, if (active) R.color.green else R.color.red)
}

fun View.successBorder() {
    background = rectangleShape(context = context, ContextCompat.getColor(context, R.color.green))
}

fun View.errorBorder() {
    background = rectangleShape(context = context, ContextCompat.getColor(context, R.color.red))
}

fun View.noneBorder() {
    background =
        rectangleShape(context = context, ContextCompat.getColor(context, R.color.gray_light2))
}

fun rectangleShape(context: Context, borderColor: Int): GradientDrawable {
    val shape = GradientDrawable()
    shape.shape = GradientDrawable.RECTANGLE
    shape.cornerRadius = 8f.dp(context)
    shape.setColor(Color.WHITE)
    shape.setStroke(4, borderColor)
    return shape
}


fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible)
        View.VISIBLE
    else
        View.GONE
}

fun View.inVisible(inVisible: Boolean) {
    visibility = if (inVisible)
        View.INVISIBLE
    else
        View.VISIBLE
}

fun ProgressBar.start(start: Boolean) {
    this.isVisible(start)
    if (start) {
        this.isIndeterminate = true
    } else {
        this.progress = 0
    }
}

fun View.setHexColor(hex: String) {
    val color = Color.parseColor(hex.substring(0..6))
    val r: Int = Color.red(color)
    val g: Int = Color.green(color)
    val b: Int = Color.blue(color)
    backgroundTintList = ColorStateList.valueOf(Color.rgb(r, g, b))
}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.setContent(content: String?) {
    if (content.orEmpty().isEmpty())
        return

    this.isFocusable = true
    this.isFocusableInTouchMode = true
    this.settings.javaScriptEnabled = true
    this.settings.loadsImagesAutomatically = true

    this.loadDataWithBaseURL(
        null,
        "<style>img{max-width: 100%}</style>$content", "text/html", "UTF-8", null
    )
}
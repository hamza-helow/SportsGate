package com.souqApp.infra.extension

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.souqApp.R


fun TextInputLayout.activeBorder(context: Context, active: Boolean) {
    this.boxStrokeColor =
        ContextCompat.getColor(context, if (active) R.color.green else R.color.red)
}

fun ActionBar.setup(
    homeAsUpEnabled: Boolean = true,
    showHomeEnabled: Boolean = true,
    showTitleEnabled: Boolean = false,
) {
    setDisplayHomeAsUpEnabled(homeAsUpEnabled)
    setDisplayShowHomeEnabled(showHomeEnabled)
    setDisplayShowTitleEnabled(showTitleEnabled)

}


fun View.successBorder() {
    background = rectangleShape(context = context, ContextCompat.getColor(context, R.color.green))
}

fun View.errorBorder() {
    background = rectangleShape(context = context,  ContextCompat.getColor(context, R.color.red))
}

fun View.noneBorder() {
    background = rectangleShape(context = context,  ContextCompat.getColor(context, R.color.gray_light2))
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
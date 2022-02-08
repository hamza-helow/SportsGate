package com.souqApp.infra.extension

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.souqApp.R
import android.graphics.drawable.shapes.OvalShape

import android.graphics.drawable.ShapeDrawable


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
    setDisplayHomeAsUpEnabled(homeAsUpEnabled)
    setDisplayShowHomeEnabled(showHomeEnabled)
    setDisplayShowTitleEnabled(showTitleEnabled)

}


fun View.successBorder() {
    background = rectangleShape(context = context, Color.GREEN)
}

fun View.errorBorder() {
    background = rectangleShape(context = context, Color.RED)
}

fun View.noneBorder() {
    background = rectangleShape(context = context,  ContextCompat.getColor(context, R.color.gray_light2))
}



fun rectangleShape(context: Context, borderColor: Int): GradientDrawable {
    val shape = GradientDrawable()
    shape.shape = GradientDrawable.RECTANGLE
    shape.cornerRadius = 8f.dp(context)
    shape.setColor(Color.WHITE)
    shape.setStroke(3, borderColor)
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
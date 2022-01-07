package com.souqApp.infra.extension

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible)
        View.VISIBLE
    else
        View.GONE
}
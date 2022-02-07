package com.souqApp.infra.extension

import android.content.Context
import android.util.TypedValue

import android.util.DisplayMetrics


fun Float.dp(context: Context): Float {
    val metrics: DisplayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics)
}
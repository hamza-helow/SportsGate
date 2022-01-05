package com.souqApp.presentation.common.extension

import android.util.Patterns


fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches();
}

fun String.isPasswordValid(): Boolean {
    return this.length > 8
}
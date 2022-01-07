package com.souqApp.infra.extension

import android.util.Patterns


fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches();
}

fun String.isPhone(): Boolean {
    return this.length >= 9
}

fun String.isPasswordValid(): Boolean {
    return this.length >= 8
}
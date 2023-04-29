package com.souqApp.infra.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.util.*

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.openUrl(link: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    this.startActivity(intent)
}

fun Context.setAppLocale(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)

    return createConfigurationContext(config)
}
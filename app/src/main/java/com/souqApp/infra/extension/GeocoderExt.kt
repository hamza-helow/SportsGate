package com.souqApp.infra.extension

import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Geocoder.getAddress(
    latitude: Double,
    longitude: Double,
): Address? = withContext(Dispatchers.IO) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCoroutine { cont ->
                getFromLocation(latitude, longitude, 1) {
                    cont.resume(it.firstOrNull())
                }
            }
        } else {
            suspendCoroutine { cont ->
                @Suppress("DEPRECATION")
                val address = getFromLocation(latitude, longitude, 1)?.firstOrNull()
                cont.resume(address)
            }
        }
    } catch (e: Exception) {
        null
    }
}


suspend fun Geocoder.getAddressLine(latitude: Double, longitude: Double): String {
    val address = getAddress(latitude, longitude)
    return try {
        address?.getAddressLine(0).orEmpty()
    } catch (e: Exception) {
        ""
    }
}
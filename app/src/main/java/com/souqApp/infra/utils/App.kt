package com.souqApp.infra.utils

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        MapsInitializer.initialize(baseContext)
    }
}
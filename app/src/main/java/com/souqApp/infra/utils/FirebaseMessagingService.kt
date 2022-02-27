package com.souqApp.infra.utils

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

    }

    override fun onDestroy() {
        super.onDestroy()
        startService(
            Intent(this, MFirebaseMessagingService::class.java)
        )
    }

    override fun onNewToken(token: String) {

    }

}
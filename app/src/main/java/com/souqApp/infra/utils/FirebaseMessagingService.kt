package com.souqApp.infra.utils

import android.annotation.SuppressLint
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.souqApp.R
import android.app.NotificationManager
import android.content.Context
import android.app.NotificationChannel
import android.os.Build
import com.souqApp.presentation.main.MainActivity
import android.app.PendingIntent


class MFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage)
    }

    override fun onDestroy() {
        super.onDestroy()
        startService(
            Intent(this, MFirebaseMessagingService::class.java)
        )
    }

    override fun onNewToken(token: String) {
        //ToDo send token to server

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(remoteMessage: RemoteMessage) {

        val data = remoteMessage.data
        val type = data["notify_type"]
        val redirectId = data["redirectId"] ?: ""
        val title = getString(R.string.app_name)
        val body = data["body"] ?: ""

        val pendingIntent =
            PendingIntent.getActivity(
                this,
                1001,
                getIntent(type = type, title = title, body = body, redirectId = redirectId),
                PendingIntent.FLAG_ONE_SHOT
            )

        val channelId = getString(R.string.default_notification_channel_id)
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId).apply {
                setSmallIcon(R.drawable.close_icon)
                setContentTitle(title)
                setContentText(body)
                setAutoCancel(true)
                setContentIntent(pendingIntent)
            }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun getIntent(type: String?, title: String, body: String, redirectId: String): Intent? {
        return when (type) {
            "1" -> generalIntent(title, body, redirectId)  //general
            "2" -> couponIntent(title, body, redirectId)  //coupon
            "3" -> orderIntent(title, body, redirectId)  //order
            "4" -> productIntent(title, body, redirectId)  //product
            else -> null
        }
    }

    private fun productIntent(title: String, body: String, redirectId: String): Intent {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("BODY", title)
        intent.putExtra("MESSAGE", body)
        return intent
    }

    private fun orderIntent(title: String, body: String, redirectId: String): Intent {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("BODY", title)
        intent.putExtra("MESSAGE", body)
        return intent
    }

    private fun couponIntent(title: String, body: String, redirectId: String): Intent {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("BODY", title)
        intent.putExtra("MESSAGE", body)
        return intent
    }

    private fun generalIntent(title: String, body: String, redirectId: String): Intent {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("BODY", title)
        intent.putExtra("MESSAGE", body)
        return intent
    }

}
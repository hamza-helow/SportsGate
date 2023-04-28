package com.souqApp.infra.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.souqApp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage)
    }

    override fun onNewToken(token: String) {
        sharedPrefs.firebaseToken(token)
    }


    private fun sendNotification(remoteMessage: RemoteMessage) {

        val data = remoteMessage.data
        val type = data["notify_type"]
        val redirectId = data["redirect_id"] ?: ""
        val title = remoteMessage.notification?.body.toString()

        val intent = getIntent(type = type, redirectId = redirectId)
        val channelId = getString(R.string.default_notification_channel_id)
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId).apply {
                setContentTitle(title)
                setAutoCancel(true)
                if (intent != null)
                    setContentIntent(intent)

                setSmallIcon(R.drawable.ic_launcher_foreground)
            }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(1, notificationBuilder.build())
    }


    private fun getIntent(type: String?, redirectId: String): PendingIntent? {
        return when (type) {
            "1" -> mainIntent() //general
            "2" -> mainIntent()  //coupon
            "3" -> orderIntent(redirectId)  //order
            "4" -> productIntent(redirectId)  //product
            else -> null
        }
    }

    private fun mainIntent(): PendingIntent {
        return NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.homeFragment)
            .createPendingIntent()
    }

    private fun productIntent(redirectId: String): PendingIntent {
        return NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.product_details)
            .setArguments(bundleOf("productId" to redirectId.toInt()))
            .createPendingIntent()
    }

    private fun orderIntent(redirectId: String): PendingIntent {
        return NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.orderDetailsFragment)
            .setArguments(bundleOf("orderId" to redirectId.toInt()))
            .createPendingIntent()
    }
}


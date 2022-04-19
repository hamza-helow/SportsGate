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
import android.app.PendingIntent
import android.util.Log
import androidx.annotation.RequiresApi
import com.souqApp.presentation.main.MainActivity
import com.souqApp.presentation.order_details.OrderDetailsActivity
import com.souqApp.presentation.product_details.ProductDetailsFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage)
        Log.e(APP_TAG, "notification")
    }

    override fun onNewToken(token: String) {
        sharedPrefs.firebaseToken(token)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(remoteMessage: RemoteMessage) {

        val data = remoteMessage.data
        val type = data["notify_type"]
        val redirectId = data["redirect_id"] ?: ""
        val title = remoteMessage.notification?.body.toString()
        val body = ""

        Log.e(APP_TAG, data.toString())

        val intent = getIntent(type = type, redirectId = redirectId)

        val pendingIntent = if (intent == null) null else
            PendingIntent.getActivity(
                this,
                1001,
                intent,
                PendingIntent.FLAG_ONE_SHOT
            )

        val channelId = getString(R.string.default_notification_channel_id)
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId).apply {
                setContentTitle(title)
                setContentText(body)
                setAutoCancel(true)
                if (pendingIntent != null)
                    setContentIntent(pendingIntent)

                setSmallIcon(R.drawable.ic_launcher_foreground)
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



    private fun getIntent(type: String?, redirectId: String): Intent? {
        return when (type) {
            "1" -> mainIntent() //general
            "2" -> mainIntent()  //coupon
            "3" -> orderIntent(redirectId)  //order
            "4" -> productIntent(redirectId)  //product
            else -> null
        }
    }

    private fun mainIntent(): Intent {
        return Intent(this, MainActivity::class.java)
    }

    private fun productIntent(redirectId: String): Intent {
        val intent = Intent(this, ProductDetailsFragment::class.java)
        intent.putExtra(ID_PRODUCT, redirectId.toInt())
        return intent
    }

    private fun orderIntent(redirectId: String): Intent {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        intent.putExtra(ID_ORDER, redirectId.toInt())

        return intent
    }

}


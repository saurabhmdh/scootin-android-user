package com.scootin.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.scootin.R
import com.scootin.util.constants.AppConstants
import com.scootin.util.constants.IntentConstants.openDirectOrderDetail
import com.scootin.util.constants.IntentConstants.openOrderDetail
import com.scootin.view.activity.MainActivity
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.data.isNotEmpty()) {
            val type = remoteMessage.data["type"]
            Timber.i("Notification type $type")
            when (type) {
                "UPDATED_NORMAL_ORDER_TO_USER" -> {
                    remoteMessage.data["order_id"]?.let {
                        addNewOrderNotification(it, true)
                    }
                }
                "UPDATED_DIRECT_ORDER_TO_USER" -> {
                    remoteMessage.data["order_id"]?.let {
                        addNewOrderNotification(it, false)
                    }
                }
                "UPDATED_CITY_WIDE_ORDER_TO_USER" -> {
                    //We need to write code incase of City wide order
                }
                "USER_DISABLED" -> {
                    WorkManager.getInstance().enqueue(OneTimeWorkRequestBuilder<DisableWorker>().build())
                    val localIntent = Intent(AppConstants.INTENT_ACTION_USER_DISABLED)
                    LocalBroadcastManager.getInstance(baseContext).sendBroadcast(localIntent)
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        Timber.i("Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Timber.i("sendRegistrationTokenToServer($token)")
    }

    private fun addNewOrderNotification(orderId: String, isNormal: Boolean) {
        val intent = Intent(this, MainActivity::class.java)

        if (isNormal) {
            intent.data = openOrderDetail(orderId)
        } else {
            intent.data = openDirectOrderDetail(orderId)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_app)
            .setContentTitle(getString(R.string.order_update_message))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

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

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}
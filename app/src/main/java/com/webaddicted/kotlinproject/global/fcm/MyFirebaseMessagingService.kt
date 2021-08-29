package com.webaddicted.kotlinproject.global.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.GlobalUtility.Companion.getTwoDigitRandomNo
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.global.constant.AppConstant.Companion.NOTIFICATION_CHANNEL_ID
import com.webaddicted.kotlinproject.model.bean.common.NotificationData

/**
 * This class is used for push notification implementation
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var mNotificationData: NotificationData? = null
//    private var mAlert: String? = null
//    private var mSound: String? = null

    companion object {
        private val TAG = MyFirebaseMessagingService::class.java.simpleName
    }

    /**
     * This method is invoked whenever the device receives the push notification
     * @param remoteMessage
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Lg.d(TAG, "messagess")
        Lg.d(TAG, "aaaaaaaaaaaaaaaaa messagess")

//        if (remoteMessage != null && remoteMessage!!.getData() != null) {
//            val payload = remoteMessage!!.getData().get("payload")
//            mNotificationData = GlobalUtility.stringToJson(payload, NotificationData::class.java)
//            mAlert = remoteMessage!!.getData().get("alert")
//            mSound = remoteMessage!!.getData().get("sound")
//            showNotification(this, mNotificationData)
//        } else {
        mNotificationData = NotificationData().apply {
            id = 1
            type = getString(R.string.dummyText)
            title = remoteMessage.data.get("title")!!
            msg = remoteMessage.data.get("message")!!
        }
            showNotification(this, mNotificationData)
//        }

    }

    /**
     * This method is used to generate notification passing in the Pending Intent and other required information
     * @param context
     * @param mNotificationData
     */
    private fun showNotification(context: Context, mNotificationData: NotificationData?) {
        val intent = GlobalUtility.getIntentForPush(context, mNotificationData)
        if (intent != null) {
            val pendingIntent = PendingIntent.getActivity(
                context, getTwoDigitRandomNo(), intent,
                PendingIntent.FLAG_ONE_SHOT
            )
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(context)
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
            notificationBuilder.setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )
            notificationBuilder.setBadgeIconType(R.mipmap.ic_launcher_round)
            notificationBuilder.setContentTitle(mNotificationData?.title)
//            if (mAlert != null) {
                notificationBuilder.setContentText(mNotificationData?.msg)
                notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(mNotificationData?.msg))
//            }
            notificationBuilder.setAutoCancel(true)
            notificationBuilder.setSound(defaultSoundUri)
            notificationBuilder.setVibrate(longArrayOf(1000, 1000))
            notificationBuilder.setContentIntent(pendingIntent)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "NOTIFICATION_CHANNEL_NAME",
                    importance
                )
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern = longArrayOf(1000, 1000)
                notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
                notificationManager.createNotificationChannel(notificationChannel)
            }
            notificationManager.notify(
                getTwoDigitRandomNo()/*Id of Notification*/,
                notificationBuilder.build()
            )
        }
    }


}
package com.webaddicted.kotlinproject.global.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", 0)
        // if you want cancel notification
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)
    }

    companion object {
        const val ACTION_CALL= "ACTION_CALL"
        const val ACTION_DISMISS= "ACTION_DISMISS"
        const val ACTION_NO_IDEA= "ACTION_NO_IDEA"

    }
}
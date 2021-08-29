package com.webaddicted.kotlinproject.global.services

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.view.activity.HomeActivity

class ForegroundService : Service() {
    companion object {
        val TAG = ForegroundService::class.java.simpleName
        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_PLAY = "ACTION_PLAY"
    }
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Lg.d(TAG,"My foreground service onCreate()")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.action
        if (action != null) when (action) {
            ACTION_START_FOREGROUND_SERVICE -> {
                startForegroundService()
                GlobalUtility.showToast("Foreground service is started.")
            }
            ACTION_STOP_FOREGROUND_SERVICE -> {
                stopForegroundService()
                GlobalUtility.showToast("Foreground service is stopped.")
            }
            ACTION_PLAY -> GlobalUtility.showToast("You click Play button.")
            ACTION_PAUSE -> GlobalUtility.showToast("You click Pause button.")
        }
        return super.onStartCommand(intent, flags, startId)
    }

    /* Used to build and start foreground service. */
    private fun startForegroundService() {
        Lg.d(TAG,"Start foreground service.")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("my_service", "My Background Service")
        } else { // Create notification default intent.
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            // Create notification builder.
            val builder =
                NotificationCompat.Builder(this)
            // Make notification show big text.
            val bigTextStyle =
                NotificationCompat.BigTextStyle()
            bigTextStyle.setBigContentTitle("Music player implemented by foreground service.")
            bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.")
            // Set big text style.
            builder.setStyle(bigTextStyle)
            builder.setWhen(System.currentTimeMillis())
            builder.setSmallIcon(com.webaddicted.kotlinproject.R.mipmap.ic_launcher_round)
            builder.setAutoCancel(true)
            val largeIconBitmap =
                BitmapFactory.decodeResource(resources, com.webaddicted.kotlinproject.R.mipmap.ic_launcher_round)
            builder.setLargeIcon(largeIconBitmap)
            // Make the notification max priority.
            builder.priority = NotificationCompat.PRIORITY_MAX
            // Make head-up notification.
            builder.setFullScreenIntent(pendingIntent, true)
            // Add Play button intent in notification.
            val playIntent = Intent(this, ForegroundService::class.java)
            playIntent.action = ACTION_PLAY
            val pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0)
            val playAction =
                NotificationCompat.Action(
                    R.drawable.ic_media_play,
                    "Play",
                    pendingPlayIntent
                )
            builder.addAction(playAction)
            // Add Pause button intent in notification.
            val pauseIntent = Intent(this, ForegroundService::class.java)
            pauseIntent.action = ACTION_PAUSE
            val pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0)
            val prevAction =
                NotificationCompat.Action(
                    R.drawable.ic_media_pause,
                    "Pause",
                    pendingPrevIntent
                )
            builder.addAction(prevAction)
            // Build the notification.
            val notification = builder.build()
            // Start foreground service.
            startForeground(1, notification)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        channelName: String
    ) {
        val resultIntent = Intent(this, HomeActivity::class.java)
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(resultIntent)
        val resultPendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        val chan =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager =
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
        notificationBuilder.setAutoCancel(true)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_media_play)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MAX)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(resultPendingIntent) //intent
            .build()
        val notificationManager =
            NotificationManagerCompat.from(this)
        notificationManager.notify(1, notificationBuilder.build())
        startForeground(1, notification)
    }

    private fun stopForegroundService() {
        Log.d(TAG,"Stop foreground service.")
        // Stop foreground service and remove the notification.
        stopForeground(true)
        // Stop the foreground service.
        stopSelf()
    }


}

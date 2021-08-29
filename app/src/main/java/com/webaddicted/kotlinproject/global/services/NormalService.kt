package com.webaddicted.kotlinproject.global.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.Nullable

class NormalService : Service() {
    private var player: MediaPlayer? = null
    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Let it continue running until it is stopped.
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        // This will play the ringtone continuously until we stop the service.
        player?.isLooping = true
        // It will start the player
        player?.start()
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show()
        //sticky use when phone is turn off...when user on phone then it try to start service
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show()
    }
}
package com.webaddicted.kotlinproject.global.services

import android.app.IntentService
import android.content.Intent
import android.os.SystemClock
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.webaddicted.kotlinproject.view.fragment.ServiceFrm


class IntentTypeService: IntentService("IntentTypeService") {
    override fun onHandleIntent(intent: Intent?) {
        val message = intent?.getStringExtra("message")
        intent?.action = ServiceFrm.FILTER_ACTION_KEY
        SystemClock.sleep(3000)
        val echoMessage = "IntentService after a pause of 3 seconds echoes $message"
        intent?.putExtra("broadcastMessage", echoMessage)?.let {
            LocalBroadcastManager.getInstance(applicationContext)
                .sendBroadcast(it)
        }
    }

}
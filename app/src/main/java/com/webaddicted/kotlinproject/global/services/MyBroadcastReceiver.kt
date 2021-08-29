package com.webaddicted.kotlinproject.global.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.webaddicted.kotlinproject.global.common.Lg


/**
 * Created by Deepak Sharma(Webaddicted) on 22-04-2020.
 */
class MyBroadcastReceiver : BroadcastReceiver() {
    /**
     * Receive swipe/dismiss or delete action from user.This will not be triggered if you manually cancel the notification.
     * @param context
     * @param intent
     */
    override fun onReceive(context: Context, intent: Intent) {
        /**
         * As soon as received,remove it from shared preferences,meaning the notification no longer available on the tray for user so you do not need to worry.
         */
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("shared", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(intent.extras!!.getInt("id").toString())
        editor.commit()
        Lg.d("TAG","test  get receiver")
    }
}
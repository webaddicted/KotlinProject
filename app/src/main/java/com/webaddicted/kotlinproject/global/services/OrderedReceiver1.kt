package com.webaddicted.kotlinproject.global.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.webaddicted.kotlinproject.global.common.showToast

class OrderedReceiver1 :BroadcastReceiver(){
    override fun onReceive(context: Context?, p1: Intent?) {
        context?.showToast("Ordered receiver 1")
    }

}
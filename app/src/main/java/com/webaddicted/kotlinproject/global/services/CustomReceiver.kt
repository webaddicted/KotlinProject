package com.webaddicted.kotlinproject.global.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.global.common.showToast

class CustomReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, p1: Intent?) {
        Lg.d("TAG","receiver")
        context?.showToast(context.getString(R.string.broadcast_receiver))
    }

}
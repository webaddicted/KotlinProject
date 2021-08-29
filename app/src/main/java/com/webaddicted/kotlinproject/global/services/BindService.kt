package com.webaddicted.kotlinproject.global.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.util.*

class BindService : Service() {
    // Binder given to clients
    private val mBinder = LocalBinder()
    // Random number generator
    private val mGenerator = Random()

    /** method for clients  */
//    val randomNumber: Int
//        get() = mGenerator.nextInt(100)

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        val getService: BindService
            get() = this@BindService
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

}
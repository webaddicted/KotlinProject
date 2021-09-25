package com.webaddicted.kotlinproject.global.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * The type Network change receiver.
 * Instantiates a new Network change receiver.
 */
class NetworkChangeReceiver : BroadcastReceiver() {
    var count = 0
    companion object {
        private val TAG = NetworkChangeReceiver::class.qualifiedName

        /**
         * The constant connectivityReceiverListener.
         */
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
        fun isInternetAvailable(connectivityReceiverListener: ConnectivityReceiverListener?) {
            this.connectivityReceiverListener = connectivityReceiverListener
        }

    }

    override fun onReceive(context: Context, intent: Intent) {
        if (context.isNetworkAvailable()) {
            if (count == 0) {
                count++
                return
            }
            if (connectivityReceiverListener != null)
                connectivityReceiverListener?.onNetworkConnectionChanged(true)
        } else {
            if (count == 0) count++
            if (connectivityReceiverListener != null)
                connectivityReceiverListener?.onNetworkConnectionChanged(false)
        }
    }


    /**
     * The interface Connectivity receiver listener.
     */
    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(networkConnected: Boolean)
    }
}

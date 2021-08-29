package com.webaddicted.kotlinproject.global.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class SMSReceiver : BroadcastReceiver() {

    companion object {
        val TAG = SMSReceiver::class.java.simpleName
        private lateinit var otpReceiveListener: OTPReceiveListener
        fun requestData(otpReceiveListener: OTPReceiveListener) {
            this.otpReceiveListener = otpReceiveListener
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras!!.get(SmsRetriever.EXTRA_STATUS) as Status?
            when (status!!.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    //This is the full message
                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String?
                    otpReceiveListener.onOTPReceived(parseCode(message!!))
                    Log.d(TAG, "SUCCESS -> $message")
                    /*<#> Your ExampleApp code is: 123ABC78
                    FA+9qCX9VSu*/
                    //Extract the OTP code and send to the listener
                }
                CommonStatusCodes.TIMEOUT -> otpReceiveListener.onOTPReceivedError("TIME out")
                // Waiting for SMS timed out (5 minutes)
                CommonStatusCodes.API_NOT_CONNECTED -> otpReceiveListener.onOTPReceivedError("API NOT CONNECTED")
                CommonStatusCodes.NETWORK_ERROR -> otpReceiveListener.onOTPReceivedError("NETWORK ERROR")
                CommonStatusCodes.ERROR -> otpReceiveListener.onOTPReceivedError("SOME THING WENT WRONG")
            }
        }
    }

    private fun parseCode(message: String): String {
        val p = Pattern.compile("\\b\\d{6}\\b")
        val m = p.matcher(message)
        var code = ""
        while (m.find()) {
            code = m.group(0)!!
        }
        return code
    }

    /**
     *
     */
    interface OTPReceiveListener {
        fun onOTPReceived(otp: String)
        fun onOTPReceivedError(error: String)
    }
}
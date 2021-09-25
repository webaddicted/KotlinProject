package com.webaddicted.kotlinproject.view.deviceinfo

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Html
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmDevFeatureBinding
import com.webaddicted.kotlinproject.view.base.BaseFragment

class DeviceFeaturesFrm : BaseFragment(R.layout.frm_dev_feature) {
    private lateinit var mBinding: FrmDevFeatureBinding

    companion object {
        val TAG = DeviceFeaturesFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): DeviceFeaturesFrm {
            val fragment = DeviceFeaturesFrm()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmDevFeatureBinding
        getDeviceFeatures()
    }

    private fun getDeviceFeatures() {
        val txtColor: String = if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_NO
        ) "#000000"
        else "#FFFFFF"
        val connManager =
            activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val packageManager = activity?.packageManager
        val featureInfo =
            """<font color="$txtColor">Wifi : </font>${getAvailability(mWifi!!.isAvailable)}<br><font color="$txtColor">WIFI Direct : </font>${getAvailability(
                packageManager?.hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT)!!
            )}<br><font color="$txtColor">Bluetooth : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_BLUETOOTH
                )
            )}<br><font color="$txtColor">Bluetooth LE : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_BLUETOOTH_LE
                )
            )}<br><font color="$txtColor">GPS : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_LOCATION_GPS
                )
            )}<br><font color="$txtColor">Camera Flash : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH
                )
            )}<br><font color="$txtColor">Camera Front : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FRONT
                )
            )}<br><font color="$txtColor">Microphone : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_MICROPHONE
                )
            )}<br><font color="$txtColor">NFC : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_NFC
                )
            )}<br><font color="$txtColor">USB Host : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_USB_HOST
                )
            )}<br><font color="$txtColor">USB Accessory : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_USB_ACCESSORY
                )
            )}<br><font color="$txtColor">Multitouch : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH
                )
            )}<br><font color="$txtColor">Audio low-latency : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_AUDIO_LOW_LATENCY
                )
            )}<br><font color="$txtColor">Audio Output : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_AUDIO_OUTPUT
                )
            )}<br><font color="$txtColor">Professional Audio : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_AUDIO_PRO
                )
            )}<br><font color="$txtColor">Consumer IR : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_CONSUMER_IR
                )
            )}<br><font color="$txtColor">Gamepad Support : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_GAMEPAD
                )
            )}<br><font color="$txtColor">HIFI Sensor : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_HIFI_SENSORS
                )
            )}<br><font color="$txtColor">Printing : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_PRINTING
                )
            )}<br><font color="$txtColor">CDMA : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_TELEPHONY_CDMA
                )
            )}<br><font color="$txtColor">GSM : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_TELEPHONY_GSM
                )
            )}<br><font color="$txtColor">Finger-print : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_FINGERPRINT
                )
            )}<br><font color="$txtColor">App Widgets : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_APP_WIDGETS
                )
            )}<br><font color="$txtColor">SIP : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_SIP
                )
            )}<br><font color="$txtColor">SIP based VOIP : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_SIP_VOIP
                )
            )}<br><font color="$txtColor">Microphone : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_MICROPHONE
                )
            )}<br><font color="$txtColor">Microphone : </font>${getAvailability(
                packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
            )}<br><font color="$txtColor">Microphone : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_MICROPHONE
                )
            )}<br><font color="$txtColor">Microphone : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_MICROPHONE
                )
            )}<br><font color="$txtColor">Microphone : </font>${getAvailability(
                packageManager.hasSystemFeature(
                    PackageManager.FEATURE_MICROPHONE
                )
            )}<br>"""
        mBinding.txtDeviceFeature.text = Html.fromHtml(featureInfo, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun getAvailability(available: Boolean): String {
        return if (available) "Available"
        else "Not Supported"
    }
}

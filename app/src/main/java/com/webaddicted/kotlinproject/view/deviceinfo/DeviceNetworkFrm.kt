package com.webaddicted.kotlinproject.view.deviceinfo

import android.content.Context
import android.content.res.Configuration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.Html
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmDevNetworkBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.isNetworkAvailable
import com.webaddicted.kotlinproject.view.base.BaseFragment

class DeviceNetworkFrm : BaseFragment(R.layout.frm_dev_network) {
    private lateinit var mBinding: FrmDevNetworkBinding

    companion object {
        val TAG = DeviceNetworkFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): DeviceNetworkFrm {
            val fragment = DeviceNetworkFrm()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmDevNetworkBinding
        getNetworkInfo()
    }

    private fun getNetworkInfo() {
        val txtColor: String = if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_NO
        ) "#000000"
        else "#FFFFFF"
        var networkInfo: String
        networkInfo = if (activity?.isNetworkAvailable()!!) {
            "<font color=\"$txtColor\">Connection Status : </font> Connected<br>" +
                    "<font color=\"$txtColor\">IP Address : </font> ${GlobalUtility.getIPAddress(true)}<br>"
        } else {
            "<font color=\"$txtColor\">Connection Status : </font> Not Connected<br>" +
                    "<font color=\"$txtColor\">IP Address : </font> Unavailable<br>"
        }

        when {
            GlobalUtility.isWifiConnected(mActivity) == resources.getString(R.string.wifi) -> {
                val wifiManager =
                    activity?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                networkInfo =
                    networkInfo + "<font color=\"$txtColor\">Data Type : </font>" + resources.getString(R.string.wifi) + "<br>" +
                            "<font color=\"$txtColor\">Network Type : </font>" + resources.getString(R.string.wifi) + "<br>" +
                            "<font color=\"$txtColor\">SSID : </font>${wifiInfo.ssid}<br>" +
                            "<font color=\"$txtColor\">Link Speed : </font>${wifiInfo.ssid}<br>" +
                            "<font color=\"$txtColor\">MAC Address : </font>${GlobalUtility.getMACAddress(
                                "wlan0"
                            )}<br>" +
                            "<font color=\"$txtColor\">Link Speed : </font>${wifiInfo.linkSpeed} Mbps<br>"
            }
            GlobalUtility.isWifiConnected(mActivity) == activity?.getString(R.string.network) -> {
                networkInfo =
                    networkInfo + "<font color=\"$txtColor\">Data Type : </font>" + resources.getString(R.string.network) + "<br>" +
                            "<font color=\"$txtColor\">Network Type : </font>" + resources.getString(R.string.network) + "<br>" +
                            "<font color=\"$txtColor\">SSID : </font> Not Available<br>" +
                            "<font color=\"$txtColor\">Link Speed : </font> Not Available<br>" +
                            "<font color=\"$txtColor\">MAC Address : </font>${GlobalUtility.getMACAddress(
                                "eth0"
                            )}<br>" +
                            "<font color=\"$txtColor\">Link Speed : </font> Not Available<br>"
            }
            else -> {
                networkInfo =
                    networkInfo + "<font color=\"$txtColor\">Data Type : </font> Not Available<br>" +
                            "<font color=\"$txtColor\">Network Type : </font> Not Available<br>"
            }
        }
        mBinding.txtNetworkInfo.text = Html.fromHtml(networkInfo, Html.FROM_HTML_MODE_LEGACY)
    }
}

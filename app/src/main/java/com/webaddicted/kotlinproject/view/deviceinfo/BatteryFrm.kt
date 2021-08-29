package com.webaddicted.kotlinproject.view.deviceinfo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmDevBatteryBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment

class BatteryFrm : BaseFragment() {
    private lateinit var mBinding: FrmDevBatteryBinding
    var health = 0
    var iconSmall = 0
    var level = 0
    var plugged = 0
    var present = false
    var scale = 0
    var status = 0
    var technology: String? = null
    var temperature = 0
    var voltage = 0
    var deviceStatus = 0

    companion object {
        val TAG = BatteryFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): BatteryFrm {
            val fragment = BatteryFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_dev_battery
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmDevBatteryBinding
        init()
    }

    private fun init() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW)
        activity?.registerReceiver(mBatInfoReceiver, intentFilter)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
        }
    }

    private val mBatInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_BATTERY_LOW)
                GlobalUtility.showToast(getString(R.string.battery_low))
            else {
                deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
                iconSmall = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0)
                plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
                present = intent.extras!!.getBoolean(BatteryManager.EXTRA_PRESENT)
                scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0)
                status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0)
                technology =
                    intent.extras!!.getString(BatteryManager.EXTRA_TECHNOLOGY)
                temperature =
                    intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10
                voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
                try {
                    getBatteryInfo()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getBatteryInfo() {
        when (deviceStatus) {
            BatteryManager.BATTERY_STATUS_CHARGING -> {
                mBinding.fabBatteryCharging.visible()
                mBinding.fabBatteryCharging.setImageResource(R.drawable.ic_battery)
            }
            BatteryManager.BATTERY_STATUS_DISCHARGING, BatteryManager.BATTERY_STATUS_FULL,
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> mBinding.fabBatteryCharging.gone()
        }
        mBinding.progressBar.setProgress(level)
        mBinding.txtTemp.text =
            resources.getString(R.string.battery_temp) + temperature.toString() + "" + resources.getString(
                R.string.c_symbol
            )
        technology.let { mBinding.txtBatteryType.text = getString(R.string.battery_type) + it }
        mBinding.txtVoltage.text = getString(R.string.battery_voltage) + voltage.toString() + "mV"
        scale.let {
            mBinding.txtBatteryScale.text = "${getString(R.string.battery_scale)}  $it"
            mBinding.txtBatteryLevel.text = "$level%"
            when (health) {
                1 -> mBinding.txtBatteryHealth.text =
                    getString(R.string.battery_health) + resources.getString(
                        R.string.unknown
                    )
                2 -> mBinding.txtBatteryHealth.text =
                    getString(R.string.battery_health) + resources.getString(
                        R.string.good
                    )
                3 -> mBinding.txtBatteryHealth.text =
                    getString(R.string.battery_health) + resources.getString(
                        R.string.over_heated
                    )
                4 -> mBinding.txtBatteryHealth.text =
                    getString(R.string.battery_health) + resources.getString(
                        R.string.dead
                    )
                5 -> mBinding.txtBatteryHealth.text =
                    getString(R.string.battery_health) + resources.getString(
                        R.string.over_voltage
                    )
                6 -> mBinding.txtBatteryHealth.text =
                    getString(R.string.battery_health) + resources.getString(
                        R.string.failed
                    )
                else -> mBinding.txtBatteryHealth.text =
                    getString(R.string.battery_health) + resources.getString(
                        R.string.cold
                    )
            }
            when (plugged) {
                1 -> mBinding.txtPowerSource.text =
                    getString(R.string.battery_power) + resources.getString(
                        R.string.ac_power
                    )
                else -> mBinding.txtPowerSource.text =
                    getString(R.string.battery_power) + resources.getString(
                        R.string.battery
                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(mBatInfoReceiver)
    }
}

package com.webaddicted.kotlinproject.view.deviceinfo

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmDevBluetoothBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment

class BluetoothFrm : BaseFragment(R.layout.frm_dev_bluetooth) {
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private lateinit var mBinding: FrmDevBluetoothBinding
    private val REQUEST_ENABLE_BT = 1

    companion object {
        val TAG = BluetoothFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): BluetoothFrm {
            val fragment = BluetoothFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmDevBluetoothBinding
        init()
        clickListener()
    }

    private fun init() {
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        activity?.registerReceiver(mBluetoothStateReceiver, filter)
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        mBinding.btAnimView.visible()
        getLocalBluetoothName()
    }

    private fun clickListener() {
        mBinding.btnBtOnOff.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_bt_on_off -> startActivityForResult(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                REQUEST_ENABLE_BT
            )
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @SuppressLint("HardwareIds", "WrongConstant")
    private fun getLocalBluetoothName() {

        mBinding.txtBtName.text =
            getString(R.string.name) + "                                :     " + mBluetoothAdapter.name
        mBinding.txtBtAddress.text =
            getString(R.string.address) + "                             :     " + mBluetoothAdapter.address
        mBinding.txtBtScanMode.text =
            getString(R.string.scan_mode) + "                        :     " + mBluetoothAdapter.scanMode.toString()

        if (mBluetoothAdapter.isEnabled) {
            mBinding.txtBtState.text =
                getString(R.string.bt_state) + "              :     " + resources.getString(R.string.switch_on)
            mBinding.btnBtOnOff.isEnabled = false
            mBinding.btnBtOnOff.setBackgroundColor(
                ContextCompat.getColor(
                    mActivity,
                    R.color.divider
                )
            )
        } else {
            mBinding.txtBtState.text =
                getString(R.string.bt_state) + "              :     " + resources.getString(R.string.switch_off)
            mBinding.btnBtOnOff.text = resources.getString(R.string.turn_on_bluetooth)
            mBinding.btnBtOnOff.setBackgroundColor(
                ContextCompat.getColor(
                    mActivity,
                    R.color.app_color
                )
            )
        }

        if (!mBluetoothAdapter.isDiscovering) {
            mBinding.txtBtDiscover.text =
                getString(R.string.bt_discover) + "          :     " + resources.getString(R.string.switch_off)
        }
    }

    private val mBluetoothStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    BluetoothAdapter.STATE_OFF -> {
                        mBinding.txtBtState.text =
                            getString(R.string.bt_state) + "              :     " + resources.getString(
                                R.string.switch_off
                            )
                        mBinding.btnBtOnOff.isEnabled = true
                        mBinding.btnBtOnOff.setBackgroundColor(
                            ContextCompat.getColor(
                                mActivity,
                                R.color.colorPrimary
                            )
                        )
                    }
                    BluetoothAdapter.STATE_ON -> {
                        mBinding.btnBtOnOff.isEnabled = false
                        mBinding.btnBtOnOff.setBackgroundColor(
                            ContextCompat.getColor(
                                mActivity,
                                R.color.divider
                            )
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(mBluetoothStateReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_ENABLE_BT) {
//            if (resultCode == Activity.RESULT_OK) mBinding.btAnimView.visible()
//            else mBinding.btAnimView.gone()
//        }
        if (mBluetoothAdapter.isEnabled) {
            mBinding.btnBtOnOff.isEnabled = false
            mBinding.btnBtOnOff.setBackgroundColor(
                ContextCompat.getColor(
                    mActivity,
                    R.color.divider
                )
            )
        } else {
            mBinding.btnBtOnOff.isEnabled = true
            mBinding.btnBtOnOff.setBackgroundColor(
                ContextCompat.getColor(
                    mActivity,
                    R.color.app_color
                )
            )
        }
    }
}

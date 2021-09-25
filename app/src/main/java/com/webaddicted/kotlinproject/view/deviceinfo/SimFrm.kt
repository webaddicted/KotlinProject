package com.webaddicted.kotlinproject.view.deviceinfo

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmDevSimBinding
import com.webaddicted.kotlinproject.global.common.PermissionHelper
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.model.bean.deviceinfo.SimInfo
import com.webaddicted.kotlinproject.view.adapter.SimAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment

class SimFrm : BaseFragment(R.layout.frm_dev_sim) {
    private lateinit var mAdapter: SimAdapter
    private lateinit var mBinding: FrmDevSimBinding
    private var telephonyManager: TelephonyManager? = null
    private var simInfoDataList: ArrayList<SimInfo>? = ArrayList()

    companion object {
        val TAG = SimFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): SimFrm {
            val fragment = SimFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmDevSimBinding
        init()
    }

    private fun init() {
        telephonyManager =
            activity?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        setAdapter()
        checkPermission()
    }

    private fun setAdapter() {
        mAdapter = SimAdapter(simInfoDataList)
        mBinding.rvSimData.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mBinding.rvSimData.adapter = mAdapter
    }

    private fun checkPermission() {
        val locationList = java.util.ArrayList<String>()
        locationList.add(Manifest.permission.READ_PHONE_STATE)
        PermissionHelper.requestMultiplePermission(
            mActivity,
            locationList,
            object : PermissionHelper.Companion.PermissionListener {
                override fun onPermissionGranted(mCustomPermission: List<String>) {
                    retrieveSimInformation(telephonyManager!!)
                }

                override fun onPermissionDenied(mCustomPermission: List<String>) {

                }
            })
    }

    @TargetApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission", "HardwareIds")
    private fun retrieveSimInformation(telephonyManager: TelephonyManager) {
        if (isSimAvailable(
                mActivity,
                0
            ) && telephonyManager.simState == TelephonyManager.SIM_STATE_READY
        ) {
            mBinding.cvSimDataParent.visible()
            if (mBinding.llEmptyState.isShown)
                mBinding.llEmptyState.visible()

            simInfoDataList?.add(
                SimInfo("SIM 1 state",
                    simState(telephonyManager.simState)
                )
            )
            simInfoDataList?.add(
                SimInfo(
                    "Integrated circuit card identifier (ICCID)",
                    telephonyManager.simSerialNumber!!
                )
            )
            simInfoDataList?.add(
                SimInfo(
                    "Unique device ID (IMEI or MEID/ESN for CDMA)",
                    telephonyManager.getImei(0)
                )
            )
            simInfoDataList?.add(
                SimInfo(
                    "International mobile subscriber identity (IMSI)",
                    telephonyManager.subscriberId
                )
            )
            simInfoDataList?.add(
                SimInfo(
                    "Service provider name (SPN)",
                    telephonyManager.simOperatorName
                )
            )
            simInfoDataList?.add(
                SimInfo(
                    "Mobile country code (MCC)",
                    telephonyManager.networkCountryIso
                )
            )
            simInfoDataList?.add(
                SimInfo(
                    "Mobile operator name",
                    telephonyManager.networkOperatorName
                )
            )
            simInfoDataList?.add(
                SimInfo(
                    "Network type",
                    networkType(telephonyManager.networkType)
                )
            )

            simInfoDataList?.add(
                SimInfo(
                    "Mobile country code + mobile network code (MCC+MNC)",
                    telephonyManager.simOperator
                )
            )
            simInfoDataList?.add(
                SimInfo(
                    "Mobile station international subscriber directory number (MSISDN)",
                    telephonyManager.line1Number
                )
            )

            /*  if (isSimAvailable(mActivity, 1)) {
                    simInfoDataList?.add(SimInfo("", ""))
                        simInfoDataList?.add(SimInfo("SIM 2 state", simState(getDeviceIdBySlot(mActivity, "getSimState", 1).toInt())))
                        simInfoDataList?.add(SimInfo("Unique device ID (IMEI or MEID/ESN for CDMA)", telephonyManager.getImei(1)))
                        simInfoDataList?.add(SimInfo("Integrated circuit card identifier (ICCID)", getDeviceIdBySlot(mActivity, "getSimSerialNumber", 1)))
                        simInfoDataList?.add(SimInfo("International mobile subscriber identity (IMSI)", ""+getDeviceIdBySlot(mActivity, "getSubscriberId", 1)))
                        simInfoDataList?.add(SimInfo("Service provider name (SPN)", getDeviceIdBySlot(mActivity, "getSimOperatorName", 1)))
                        simInfoDataList?.add(SimInfo("Mobile country code (MCC)", getDeviceIdBySlot(mActivity, "getNetworkCountryIso", 1)))
                        simInfoDataList?.add(SimInfo("Mobile operator name", ""+getDeviceIdBySlot(mActivity, "getNetworkOperatorName", 1)))
                        simInfoDataList?.add(SimInfo("Network type", networkType(getDeviceIdBySlot(mActivity, "getNetworkType", 1).toInt())))
                        simInfoDataList?.add(SimInfo("Mobile country code + mobile network code (MCC+MNC)", ""+getDeviceIdBySlot(mActivity, "getSimOperator", 1)))
                        simInfoDataList?.add(SimInfo("Mobile station international subscriber directory number (MSISDN)", ""+getDeviceIdBySlot(mActivity, "getLine1Number", 1)))
                }*/
            mAdapter.notifyAdapter(simInfoDataList!!)
        } else {
            mBinding.cvSimDataParent.gone()
            mBinding.llEmptyState.visible()
        }
    }

    private fun simState(simState: Int): String {
        return when (simState) {
            0 -> "UNKNOWN"
            1 -> "ABSENT"
            2 -> "REQUIRED"
            3 -> "PUK_REQUIRED"
            4 -> "NETWORK_LOCKED"
            5 -> "READY"
            6 -> "NOT_READY"
            7 -> "PERM_DISABLED"
            8 -> "CARD_IO_ERROR"
            else -> "??? $simState"
        }
    }

    private fun networkType(simState: Int): String {
        return when (simState) {
            TelephonyManager.NETWORK_TYPE_GPRS -> "GPRS"
            TelephonyManager.NETWORK_TYPE_EDGE -> "EDGE"
            TelephonyManager.NETWORK_TYPE_CDMA -> "CDMA"
            TelephonyManager.NETWORK_TYPE_1xRTT -> "1xRTT"
            TelephonyManager.NETWORK_TYPE_IDEN -> "IDEN"

            TelephonyManager.NETWORK_TYPE_UMTS -> "UMTS"
            TelephonyManager.NETWORK_TYPE_EVDO_0 -> "EVDO 0"
            TelephonyManager.NETWORK_TYPE_EVDO_A -> "EVDO A"
            TelephonyManager.NETWORK_TYPE_HSDPA -> "HSDPA"
            TelephonyManager.NETWORK_TYPE_HSUPA -> "HSUPA"
            TelephonyManager.NETWORK_TYPE_HSPA -> "HSPA"
            TelephonyManager.NETWORK_TYPE_EVDO_B -> "EVDO B"
            TelephonyManager.NETWORK_TYPE_EHRPD -> "EHRPD"
            TelephonyManager.NETWORK_TYPE_HSPAP -> "HSPAP"

            TelephonyManager.NETWORK_TYPE_LTE -> "LTE"
            else -> "Unknown"
        }
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun isSimAvailable(context: Activity, slotId: Int): Boolean {
        val sManager =
            context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        val infoSim = sManager.getActiveSubscriptionInfoForSimSlotIndex(slotId)
        if (infoSim != null) {
            return true
        }
        return false
    }
}

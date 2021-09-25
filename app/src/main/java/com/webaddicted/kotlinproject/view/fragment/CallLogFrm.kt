package com.webaddicted.kotlinproject.view.fragment

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmSmsBinding
import com.webaddicted.kotlinproject.global.calllog.LogObject
import com.webaddicted.kotlinproject.global.calllog.LogsManager
import com.webaddicted.kotlinproject.global.common.PermissionHelper
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.adapter.CallLogAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import kotlinx.coroutines.*


class CallLogFrm : BaseFragment(R.layout.frm_sms) {
    private var callLogsBean: MutableList<LogObject>? = null
    private lateinit var mAdapter: CallLogAdapter
    private lateinit var mBinding: FrmSmsBinding

    companion object {
        val TAG = CallLogFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): CallLogFrm {
            val fragment = CallLogFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmSmsBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.call_log_title)
        setAdapter()
    }

    private fun clickListener() {
        mBinding.btnReadSms.text = "Read Call Log"
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnReadSms.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_read_sms -> checkSmsPermission()
        }
    }

    private fun setAdapter() {
        mAdapter = CallLogAdapter(callLogsBean)
        mBinding.includeSms.rvApps.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mBinding.includeSms.rvApps.adapter = mAdapter
    }

    private fun checkSmsPermission() {
        val locationList = ArrayList<String>()
        locationList.add(Manifest.permission.READ_CALL_LOG)
        locationList.add(Manifest.permission.READ_CONTACTS)
        PermissionHelper.requestMultiplePermission(
            mActivity,
            locationList,
            object : PermissionHelper.Companion.PermissionListener {
                override fun onPermissionGranted(mCustomPermission: List<String>) {
                    GlobalScope.launch(Dispatchers.Main + Job()) {
                        withContext(Dispatchers.Default) {
                            getCallLog()
                        }
                        mBinding.includeSms.imgNoDataFound.gone()
                        mAdapter.notifyAdapter(callLogsBean)
                    }
                }

                override fun onPermissionDenied(mCustomPermission: List<String>) {
                }
            })
    }

    private fun getCallLog() {
        val logsManager = LogsManager(activity)
        callLogsBean = logsManager.getLogs(LogsManager.ALL_CALLS)
    }
}

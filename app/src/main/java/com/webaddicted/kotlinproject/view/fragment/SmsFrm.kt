package com.webaddicted.kotlinproject.view.fragment

import android.Manifest
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmSmsBinding
import com.webaddicted.kotlinproject.global.common.PermissionHelper
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.model.bean.common.SMSBean
import com.webaddicted.kotlinproject.view.adapter.SMSAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment


class SmsFrm : BaseFragment() {
    private lateinit var mAdapter: SMSAdapter
    private lateinit var mBinding: FrmSmsBinding
    private var smsBean: ArrayList<SMSBean> = ArrayList()

    companion object {
        val TAG = SmsFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): SmsFrm {
            val fragment = SmsFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_sms
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmSmsBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.sms_title)
        setAdapter()
    }

    private fun clickListener() {
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
        mAdapter = SMSAdapter(smsBean)
        mBinding.includeSms.rvApps.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mBinding.includeSms.rvApps.adapter = mAdapter
    }

    private fun checkSmsPermission() {
        val locationList = ArrayList<String>()
        locationList.add(Manifest.permission.SEND_SMS)
        locationList.add(Manifest.permission.RECEIVE_SMS)
        locationList.add(Manifest.permission.READ_SMS)
        PermissionHelper.requestMultiplePermission(
            mActivity,
            locationList,
            object : PermissionHelper.Companion.PermissionListener {
                override fun onPermissionGranted(mCustomPermission: List<String>) {
                    getAllSms()
                }

                override fun onPermissionDenied(mCustomPermission: List<String>) {
                }
            })
    }

    fun getAllSms(): List<SMSBean>? {
        val message = Uri.parse("content://sms/")
        val cr: ContentResolver = activity?.contentResolver!!
        val cursor: Cursor = cr.query(message, null, null, null, null)!!
        activity?.startManagingCursor(cursor)
        val totalSMS: Int = cursor.count
        if (cursor.moveToFirst()) {
            for (i in 0 until totalSMS) {
                val number =
                    cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)).toString()
                val body =
                    cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)).toString()
                val date =
                    cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE)).toString()
                val id =
                    cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.THREAD_ID))
                        .toString()
                val status =
                    cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.STATUS)).toString()
                val read =
                    cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.READ)).toString()
                val seen =
                    cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.SEEN)).toString()
                var smsTypes = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.TYPE))
                smsBean.add(
                    SMSBean().apply {
                    smsId = id
                    smsNo = number
                    smsBody = body
                    smsDate = date
                    smsStatus = status
                    smsRead = read
                    smsSeen = seen
                    smsType = smsTypes
                })
                cursor.moveToNext()
            }
        }
        cursor.close()
        if (smsBean != null) {
            mBinding.includeSms.imgNoDataFound.gone()
            mAdapter.notifyAdapter(smsBean)
        }
        return smsBean
    }
}


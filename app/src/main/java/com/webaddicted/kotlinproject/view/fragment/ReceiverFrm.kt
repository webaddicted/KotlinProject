package com.webaddicted.kotlinproject.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmReceiverBinding
import com.webaddicted.kotlinproject.global.common.NetworkChangeReceiver
import com.webaddicted.kotlinproject.global.common.showToast
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.global.services.CustomReceiver
import com.webaddicted.kotlinproject.global.services.OrderedReceiver1
import com.webaddicted.kotlinproject.global.services.OrderedReceiver2
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.lang.reflect.InvocationTargetException


class ReceiverFrm : BaseFragment(R.layout.frm_receiver) {
    private lateinit var mBinding: FrmReceiverBinding
    private val ACTION_STARTED = "com.example.android.supportv4.STARTED"

    companion object {
        val TAG = ReceiverFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): ReceiverFrm {
            val fragment = ReceiverFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmReceiverBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.receiver_title)

//        mFilter?.addAction(ACTION_UPDATE)
//        mFilter?.addAction(ACTION_STOPPED)
    }


    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnNormal.setOnClickListener(this)
        mBinding.btnCustom.setOnClickListener(this)
        mBinding.btnOrdered.setOnClickListener(this)
        mBinding.btnTurnOffData.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_normal -> normalReceiver()
            R.id.btn_custom -> customReceiver()
            R.id.btn_ordered -> orderedReceiver()
            R.id.btn_turn_off_data -> setMobileDataEnabled(activity, false)

        }
    }

    private fun normalReceiver() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        activity?.registerReceiver(NetworkChangeReceiver(), intentFilter)
        activity?.showToast(getString(R.string.check_internet_connection))
    }

    private fun customReceiver() {
        //step1
        val mFilter = IntentFilter()
        mFilter.addAction(ACTION_STARTED)
        activity?.registerReceiver(CustomReceiver(), mFilter)
        // step 2
        val intent = Intent(ACTION_STARTED)
        activity?.sendBroadcast(intent)
    }

    private fun orderedReceiver() {
        // STEP 1
        val ACTION_UPDATE = "com.example.android.supportv4.UPDATE"
        val mFilter = IntentFilter()
        mFilter.priority = 1
        mFilter.addAction(ACTION_UPDATE)
        val mFilter2 = IntentFilter()
        mFilter2.priority = 2
        mFilter2.addAction(ACTION_UPDATE)
        activity?.registerReceiver(OrderedReceiver1(), mFilter)
        activity?.registerReceiver(OrderedReceiver2(), mFilter2)
        // STEP 2
        val intent = Intent()
        intent.action = ACTION_UPDATE
        activity?.sendOrderedBroadcast(intent, null, object : BroadcastReceiver() {
            @SuppressLint("NewApi")
            override fun onReceive(context: Context, intent: Intent) {
                /*
                               * to capture result after all broadreceivers are finished
                               * executing
                               */
            }
        }, null, Activity.RESULT_OK, null, null)
    }

    @Throws(
        ClassNotFoundException::class,
        NoSuchFieldException::class,
        IllegalAccessException::class,
        NoSuchMethodException::class,
        InvocationTargetException::class
    )
    private fun setMobileDataEnabled(context: Context?, enabled: Boolean) {
        val conman = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val conmanClass = Class.forName(conman.toString())
        val connectivityManagerField = conmanClass.getDeclaredField("mService")
        connectivityManagerField.isAccessible = true
        val connectivityManager = connectivityManagerField.get(conman)
        val connectivityManagerClass = Class.forName(connectivityManager.javaClass.name)
        val setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod(
            "setMobileDataEnabled",
            java.lang.Boolean.TYPE
        )
        setMobileDataEnabledMethod.isAccessible = true

        setMobileDataEnabledMethod.invoke(connectivityManager, enabled)
    }
}


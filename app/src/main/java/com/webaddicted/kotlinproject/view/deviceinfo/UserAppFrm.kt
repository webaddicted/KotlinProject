package com.webaddicted.kotlinproject.view.deviceinfo

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmDevUserAppBinding
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.model.bean.deviceinfo.DeviceInfo
import com.webaddicted.kotlinproject.view.activity.DeviceInfoActivity
import com.webaddicted.kotlinproject.view.adapter.AppsAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import kotlinx.coroutines.*

class UserAppFrm : BaseFragment() {
    private val appList: ArrayList<DeviceInfo>? = null
    private lateinit var mAdapter: AppsAdapter
    private var appType: Int? = 0
    private lateinit var mBinding: FrmDevUserAppBinding

    companion object {
        val TAG = UserAppFrm::class.java.simpleName
        const val APPS_TYPE = "AppInfo"
        fun getInstance(appsType: Int): UserAppFrm {
            val fragment = UserAppFrm()
            val bundle = Bundle()
            bundle.putInt(APPS_TYPE, appsType)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_dev_user_app
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmDevUserAppBinding
        init()
    }

    private fun init() {
        appType = arguments?.getInt(APPS_TYPE)
        setAdapter()
//        showApiLoader()
        val lists = ArrayList<DeviceInfo>()
        GlobalScope.launch(Dispatchers.Main + Job()) {
            val appList = withContext(Dispatchers.Default) {
                (activity as DeviceInfoActivity).getAppsList()
                    .filterTo(lists) { it.flags == appType }
            }
            mBinding.imgNoDataFound.gone()
            mAdapter.notifyAdapter(appList)
        }
    }

    private fun setAdapter() {
        mAdapter = AppsAdapter(activity!!, appType!!, appList)
        mBinding.rvApps.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mBinding.rvApps.adapter = mAdapter

    }


}

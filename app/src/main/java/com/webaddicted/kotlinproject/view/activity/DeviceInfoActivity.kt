package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.ViewPager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmDeviceInfoBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.global.constant.AppConstant.Companion.IS_USER_COME_FROM_SYSTEM_APPS
import com.webaddicted.kotlinproject.global.constant.AppConstant.Companion.IS_USER_COME_FROM_USER_APPS
import com.webaddicted.kotlinproject.global.misc.ViewPagerAdapter
import com.webaddicted.kotlinproject.model.bean.deviceinfo.DeviceInfo
import com.webaddicted.kotlinproject.view.base.BaseActivity
import com.webaddicted.kotlinproject.view.deviceinfo.*
import java.util.*

class DeviceInfoActivity : BaseActivity(R.layout.frm_device_info) {
    private lateinit var mBinding: FrmDeviceInfoBinding

    companion object {
        val TAG = DeviceInfoActivity::class.java.simpleName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, DeviceInfoActivity::class.java))
        }
    }

    override fun onBindTo(binding: ViewDataBinding) {
        mBinding = binding as FrmDeviceInfoBinding
        init()
        clickListener()
    }


    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.device_info_title)
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
//        mBinding.viewPager.offscreenPageLimit=20
        setupViewPager(mBinding.viewPager)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)

        mBinding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                mBinding.viewPager.setCurrentItem(position, false)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> onBackPressed()
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(BatteryFrm(), "Battery")
        adapter.addFragment(BluetoothFrm(), "Bluetooth")
        adapter.addFragment(CameraFrm(), "Camera")
        adapter.addFragment(DeviceDetailsFrm(), "Details")
        adapter.addFragment(DeviceFeaturesFrm(), "Feature")
        adapter.addFragment(DeviceNetworkFrm(), "Network")
        adapter.addFragment(DisplayFrm(), "Display")
        adapter.addFragment(OSFrm(), "OS")
        adapter.addFragment(SimFrm(), "SIM")
        adapter.addFragment(StorageFrm(), "Storage")
        adapter.addFragment(
            UserAppFrm.getInstance(
                IS_USER_COME_FROM_SYSTEM_APPS
            ), "System App")
        adapter.addFragment(
            UserAppFrm.getInstance(
                IS_USER_COME_FROM_USER_APPS
            ), "User App")
        adapter.addFragment(CPUFrm(), "Processor")
        viewPager.adapter = adapter
    }

    fun getAppsList(): MutableList<DeviceInfo> {
        val deviceInfos: MutableList<DeviceInfo> =
           ArrayList()
        val flags =
            PackageManager.GET_META_DATA or PackageManager.GET_SHARED_LIBRARY_FILES
        val pm: PackageManager = packageManager!!
        val applications =
            pm.getInstalledApplications(flags)
        for (appInfo in applications) {
            if (appInfo.flags and ApplicationInfo.FLAG_SYSTEM == 1) { // System application
                val icon = pm.getApplicationIcon(appInfo)
                deviceInfos.add(
                    DeviceInfo(
                        1,
                        icon,
                        pm.getApplicationLabel(appInfo).toString(),
                        appInfo.packageName
                    )
                )
            } else { // Installed by User
                val icon = pm.getApplicationIcon(appInfo)
                deviceInfos.add(
                    DeviceInfo(
                        2,
                        icon,
                        pm.getApplicationLabel(appInfo).toString(),
                        appInfo.packageName
                    )
                )
            }
        }
        return deviceInfos
    }
//    suspend fun loadData(txtLaunch: TextView): String {
//        txtLaunch.setText(txtLaunch.text.toString() + "\nStep 3")
//        delay(TimeUnit.SECONDS.toMillis(3)) // imitate long running operation
//        txtLaunch.setText(txtLaunch.text.toString() + "\nStep 4")
//        return "Data is available: ${Random().nextInt()}"
//    }
}

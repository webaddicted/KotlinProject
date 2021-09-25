package com.webaddicted.kotlinproject.view.fragment

import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmServicesBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.global.services.BindService
import com.webaddicted.kotlinproject.global.services.BindService.LocalBinder
import com.webaddicted.kotlinproject.global.services.ForegroundService
import com.webaddicted.kotlinproject.global.services.IntentTypeService
import com.webaddicted.kotlinproject.global.services.NormalService
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.*


class ServiceFrm : BaseFragment() {
    private var myReceiver: MyReceiver? = null
    private var mBound: Boolean = false
    private var mService: LocalBinder? = null
    private lateinit var mBinding: FrmServicesBinding

    companion object {
        const val FILTER_ACTION_KEY = "any_key"
        val TAG = ServiceFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): ServiceFrm {
            val fragment = ServiceFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_services
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmServicesBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.services_title)
        autoStart()
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnStartNormalService.setOnClickListener(this)
        mBinding.btnStopNormalService.setOnClickListener(this)
        mBinding.btnStartBindService.setOnClickListener(this)
        mBinding.btnStopBindService.setOnClickListener(this)
        mBinding.btnStartIntentService.setOnClickListener(this)
        mBinding.btnStopIntentService.setOnClickListener(this)
        mBinding.btnStartForgroundService.setOnClickListener(this)
        mBinding.btnStopForgroundService.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_start_normal_service -> activity?.startService(
                Intent(
                    activity,
                    NormalService::class.java
                )
            )
            R.id.btn_stop_normal_service -> activity?.stopService(
                Intent(
                    activity,
                    NormalService::class.java
                )
            )
            R.id.btn_start_bind_service -> {
                val intent = Intent(activity, BindService::class.java)
                activity?.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
            }
            R.id.btn_stop_bind_service -> activity?.unbindService(mConnection)
            R.id.btn_start_intent_service -> startIntentService()
            R.id.btn_stop_intent_service -> activity?.unregisterReceiver(myReceiver)
            R.id.btn_start_forground_service -> {
                val intent = Intent(activity, ForegroundService::class.java)
                intent.action = ForegroundService.ACTION_START_FOREGROUND_SERVICE
                activity?.startService(intent)
            }
            R.id.btn_stop_forground_service -> {
                val intent = Intent(activity, ForegroundService::class.java)
                intent.action = ForegroundService.ACTION_STOP_FOREGROUND_SERVICE
                activity?.startService(intent)
            }
        }
    }


    /** Defines callbacks for service binding, passed to bindService()  */
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as LocalBinder
            mService = binder.getService.LocalBinder()
            mBound = true
            val dateFormat = SimpleDateFormat(
                "HH:mm:ss MM/dd/yyyy",
                Locale.US
            )
            val currentTime: String = dateFormat.format(Date())
            GlobalUtility.showToast(currentTime)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    private fun startIntentService() {
        val message = "webaddicted"
        val intent = Intent(activity, IntentTypeService::class.java)
        intent.putExtra("message", message)
        activity?.startService(intent)
        myReceiver = MyReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(FILTER_ACTION_KEY)
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(myReceiver!!, intentFilter)
    }

    private inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getStringExtra("broadcastMessage")
            //mBinding.txtIntentService.setText("webaddicted : " + "\n" + message)
        }
    }
    private fun autoStart() {
        val manufacturer = Build.MANUFACTURER
        try {
            val intent = Intent()
            when {
                "xiaomi".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                    )
                }
                "oppo".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                    )
                }
                "vivo".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                    )
                }
                "Letv".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.letv.android.letvsafe",
                        "com.letv.android.letvsafe.AutobootManageActivity"
                    )
                }
                "Honor".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.optimize.process.ProtectActivity"
                    )
                }
            }
            val list: List<ResolveInfo> =
                activity?.packageManager?.queryIntentActivities(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
                )!!
            if (list.size > 0) {
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


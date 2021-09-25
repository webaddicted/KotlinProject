package com.webaddicted.kotlinproject.view.activity

//import com.google.firebase.iid.FirebaseInstanceId
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityCommonBinding
import com.webaddicted.kotlinproject.global.common.PermissionHelper
import com.webaddicted.kotlinproject.view.base.BaseActivity
import com.webaddicted.kotlinproject.view.fragment.TaskFrm
import kotlinx.coroutines.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class HomeActivity : BaseActivity(R.layout.activity_common) {

    private lateinit var mBinding: ActivityCommonBinding

    companion object {
        val TAG: String = HomeActivity::class.java.simpleName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
        fun newClearLogin(context: Activity?) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
            context?.finish()
        }
    }

    override fun onBindTo(binding: ViewDataBinding) {
        mBinding = binding as ActivityCommonBinding
//        Lg.d(TAG, "ok token - "+FirebaseInstanceId.getInstance().token)
        navigateScreen(TaskFrm.TAG)
//        checkCameraPermission()
    }

    /**
     * navigate on fragment
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            TaskFrm.TAG -> frm = TaskFrm.
            getInstance(Bundle())
        }
        if (frm != null) navigateFragment(R.id.container, frm, false)
    }
    private fun checkCameraPermission() {
        val multiplePermission = ArrayList<String>()
        multiplePermission.add(Manifest.permission.CAMERA)
        PermissionHelper.requestMultiplePermission(
            this,
            multiplePermission,
            object : PermissionHelper.Companion.PermissionListener {
                override fun onPermissionGranted(mCustomPermission: List<String>) {
                    GlobalScope.launch(Dispatchers.Main + Job()) {
                        val appList = withContext(Dispatchers.Default) {
//                            fetchCameraCharacteristics(cameraManager, id)
                        }
//                        mAdapter.notifyAdapter(appList)
                    }
                }

                override fun onPermissionDenied(mCustomPermission: List<String>) {

                }
            })
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d("TAG","Test - onCreate")
//    }
//    override fun onStart() {
//        super.onStart()
//        Log.d("TAG","Test - onStart")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.d("TAG","Test - onResume")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d("TAG","Test - onPause")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d("TAG","Test - onStop")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d("TAG","Test - onDestroy")
//    }
}
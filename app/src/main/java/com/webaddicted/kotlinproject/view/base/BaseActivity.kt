package com.webaddicted.kotlinproject.view.base

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.global.common.*
import com.webaddicted.kotlinproject.global.sociallogin.SocialLogin
import com.webaddicted.kotlinproject.global.sociallogin.auth.GoogleAuth
import com.webaddicted.kotlinproject.view.activity.SplashActivity
import org.koin.android.ext.android.inject
import java.io.File


/**
 * Created by Deepak Sharma on 01/07/19.
 */
abstract class BaseActivity(private val layoutId: Int) : AppCompatActivity(), View.OnClickListener,
    PermissionHelper.Companion.PermissionListener,
    MediaPickerUtils.ImagePickerListener {
    private val mediaPicker: MediaPickerUtils by inject()

    companion object {
        val TAG = BaseActivity::class.java.simpleName
    }

    abstract fun onBindTo(binding: ViewDataBinding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out)
        supportActionBar?.hide()
//        setNavigationColor(resources.getColor(R.color.app_color))
        fullScreen()
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE
//        )
        GlobalUtility.hideKeyboard(this)
        val binding: ViewDataBinding?
        if (layoutId != 0) {
            try {
                binding = DataBindingUtil.setContentView(this, layoutId)
                onBindTo(binding)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        getNetworkStateReceiver()
    }

    private fun fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            if (window != null) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    protected fun setNavigationColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.navigationBarColor = color
        }
    }

    /**
     * placeholder type for image
     *
     * @param placeholderType position of string array placeholder
     * @return
     */
    protected fun getPlaceHolder(placeholderType: Int): String {
        val placeholderArray = resources.getStringArray(R.array.image_loader)
        return placeholderArray[placeholderType]
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
    }

    override fun onClick(v: View) {
        GlobalUtility.avoidDoubleClicks(v)
    }

    override fun onResume() {
        super.onResume()
        if (!isNetworkAvailable())
            GlobalUtility.initSnackBar(this@BaseActivity, false)
    }

    /**
     * broadcast receiver for check internet connectivity
     *
     * @return
     */
    private fun getNetworkStateReceiver() {
        NetworkChangeReceiver.isInternetAvailable(object :
            NetworkChangeReceiver.ConnectivityReceiverListener {
            override fun onNetworkConnectionChanged(networkConnected: Boolean) {
                try {
                    if (this@BaseActivity !is SplashActivity)
                        GlobalUtility.initSnackBar(this@BaseActivity, networkConnected)
                } catch (exception: Exception) {
                    Lg.d(TAG, "getNetworkStateReceiver : $exception")
                }
            }
        })
    }


    fun checkStoragePermission() {
        val multiplePermission = ArrayList<String>()
        multiplePermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        multiplePermission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        multiplePermission.add(Manifest.permission.CAMERA)
        if (PermissionHelper.checkMultiplePermission(this, multiplePermission)) {
            FileHelper.createApplicationFolder()
            onPermissionGranted(multiplePermission)
        } else
            PermissionHelper.requestMultiplePermission(this, multiplePermission, this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }


    override fun onPermissionGranted(mCustomPermission: List<String>) {
        FileHelper.createApplicationFolder()
    }

    override fun onPermissionDenied(mCustomPermission: List<String>) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == mediaPicker.REQUEST_CAMERA_VIDEO || requestCode == mediaPicker.REQUEST_SELECT_FILE_FROM_GALLERY) {
                mediaPicker.onActivityResult(this, requestCode, resultCode, data)
            } else if (requestCode == GoogleAuth.RC_SIGN_IN || requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
                SocialLogin.onActivityResult(this, requestCode, resultCode, data)
            }
        }
    }

    override fun imagePath(filePath: List<File>) {
    }

    fun navigateFragment(layoutContainer: Int, fragment: Fragment, isEnableBackStack: Boolean) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out, R.anim.trans_right_in, R.anim.trans_right_out)
//        fragmentTransaction.setCustomAnimations(
//            R.animator.fragment_slide_left_enter,
//            R.animator.fragment_slide_left_exit,
//            R.animator.fragment_slide_right_enter,
//            R.animator.fragment_slide_right_exit
//        )
        fragmentTransaction.replace(layoutContainer, fragment)
        if (isEnableBackStack)
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        fragmentTransaction.commitAllowingStateLoss()

    }

    fun navigateAddFragment(layoutContainer: Int, fragment: Fragment, isEnableBackStack: Boolean) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out, R.anim.trans_right_in, R.anim.trans_right_out)
//        fragmentTransaction.setCustomAnimations(
//            R.animator.fragment_slide_left_enter,
//            R.animator.fragment_slide_left_exit,
//            R.animator.fragment_slide_right_enter,
//            R.animator.fragment_slide_right_exit
//        )
        fragmentTransaction.add(layoutContainer, fragment)
        if (isEnableBackStack)
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        fragmentTransaction.commitAllowingStateLoss()
    }

}
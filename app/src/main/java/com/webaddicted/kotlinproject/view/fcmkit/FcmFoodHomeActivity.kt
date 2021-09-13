package com.webaddicted.kotlinproject.view.fcmkit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityEcomHomeBinding
import com.webaddicted.kotlinproject.global.common.AppApplication.Companion.context
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.view.base.BaseActivity
import com.webaddicted.kotlinproject.view.fcmkit.MLKitFrm.Companion.MLKIT_TYPE
//import kotlinx.android.synthetic.main.nav_header_fcm.view.*
//import kotlinx.android.synthetic.main.nav_header_main.view.txt_home

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class FcmFoodHomeActivity : BaseActivity() {
    private lateinit var mBinding: ActivityEcomHomeBinding

    companion object {
        val TAG: String = FcmFoodHomeActivity::class.java.simpleName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, FcmFoodHomeActivity::class.java))
        }

        fun newClearLogin(context: Activity?) {
            val intent = Intent(context, FcmFoodHomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
            context?.finish()
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_ecom_home
    }

    override fun initUI(binding: ViewDataBinding) {
        mBinding = binding as ActivityEcomHomeBinding
        init()
        setNavigationColor(ContextCompat.getColor(context, R.color.app_color))
    }

    private fun init() {
        navigateScreen(FcmHomeFrm.TAG, Bundle())
        navigationDrawer()
    }

    private fun navigationDrawer() {
        mBinding.navView.removeHeaderView(mBinding.navView.getHeaderView(0))
        mBinding.navView.setBackgroundColor(ContextCompat.getColor(this, R.color.nav_back_color))
        mBinding.navView.addHeaderView(
            GlobalUtility.getLayoutBinding(
                this,
                R.layout.nav_header_fcm
            ).root
        )
        val navView = mBinding.navView.getHeaderView(0)
//        navView.txt_home.setOnClickListener(this)
//        navView.txt_text_recognizer.setOnClickListener(this)
//        navView.txt_face_detection.setOnClickListener(this)
//        navView.txt_object_detection.setOnClickListener(this)
//        navView.txt_image_label.setOnClickListener(this)
//        navView.txt_barcode_scanner.setOnClickListener(this)
//        navView.txt_landmark_recog.setOnClickListener(this)
//        navView.txt_language_id.setOnClickListener(this)
//        navView.txt_translation.setOnClickListener(this)
//        navView.txt_smart_reply.setOnClickListener(this)
//        navView.txt_email_auth.setOnClickListener(this)
//        navView.txt_logout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        val bundle = Bundle()
        when (v.id) {
            R.id.txt_home -> navigateScreen(FcmHomeFrm.TAG, Bundle())
            R.id.txt_text_recognizer -> {
                bundle.putString(MLKIT_TYPE, getString(R.string.text_recognizer))
                navigateScreen(MLKitFrm.TAG, bundle)
            }
            R.id.txt_face_detection -> {
                bundle.putString(MLKIT_TYPE, getString(R.string.face_detection))
                navigateScreen(MLKitFrm.TAG, bundle)
            }
            R.id.txt_object_detection -> {
                bundle.putString(MLKIT_TYPE, getString(R.string.object_detection))
                navigateScreen(MLKitFrm.TAG, bundle)
            }
            R.id.txt_image_label -> {
                bundle.putString(MLKIT_TYPE, getString(R.string.image_label))
                navigateScreen(MLKitFrm.TAG, bundle)
            }
            R.id.txt_barcode_scanner -> {
                bundle.putString(MLKIT_TYPE, getString(R.string.barcode_scanner))
                navigateScreen(MLKitFrm.TAG, bundle)
            }
            R.id.txt_landmark_recog -> {
                bundle.putString(MLKIT_TYPE, getString(R.string.landmark_recognition))
                navigateScreen(MLKitFrm.TAG, bundle)
            }
            R.id.txt_language_id -> {
                bundle.putString(MLKIT_TYPE, getString(R.string.language_id))
                navigateScreen(MLKitFrm.TAG, bundle)
            }
            R.id.txt_translation -> {
                bundle.putString(MLKIT_TYPE, getString(R.string.device_translation))
                navigateScreen(MLKitFrm.TAG, bundle)
            }
            R.id.txt_smart_reply -> {
                bundle.putString(MLKIT_TYPE, getString(R.string.smart_reply))
                navigateScreen(MLKitFrm.TAG, bundle)
            }
            R.id.txt_email_auth -> navigateScreen(FcmEmailAuthFrm.TAG, bundle)
            R.id.txt_logout -> onBackPressed()
        }
        openCloseDrawer(false)
    }

    fun openCloseDrawer(openDrawer: Boolean) {
        if (openDrawer) mBinding.drawerLayout.openDrawer(GravityCompat.START)
        else mBinding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START))
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    /**
     * navigate to welcome activity after Splash timer Delay
     */
    private fun navigateScreen(tag: String, bundle: Bundle) {
        var frm: Fragment? = null
        when (tag) {
            FcmHomeFrm.TAG -> frm = FcmHomeFrm.getInstance(Bundle())
            MLKitFrm.TAG -> frm = MLKitFrm.getInstance(bundle)
            FcmEmailAuthFrm.TAG -> frm = FcmEmailAuthFrm.getInstance(bundle)
        }
        if (frm != null) navigateFragment(R.id.container, frm, false)
    }
}
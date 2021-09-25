package com.webaddicted.kotlinproject.view.fcmkit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityCommonBinding
import com.webaddicted.kotlinproject.global.common.AppApplication.Companion.context
import com.webaddicted.kotlinproject.view.base.BaseActivity

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class FcmFoodActivity : BaseActivity(R.layout.activity_common) {
    private lateinit var mBinding: ActivityCommonBinding

    companion object {
        val TAG: String = FcmFoodActivity::class.java.simpleName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, FcmFoodActivity::class.java))
        }
        fun newClearLogin(context: Activity?) {
            val intent = Intent(context, FcmFoodActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
            context?.finish()
        }
    }

    override fun onBindTo(binding: ViewDataBinding) {
        mBinding = binding as ActivityCommonBinding
        init()
        setNavigationColor(ContextCompat.getColor(context, R.color.app_color))
    }

    private fun init() {
        navigateScreen(FcmSocialLoginFrm.TAG)
    }

    /**
     * navigate to welcome activity after Splash timer Delay
     */
    private fun navigateScreen(tag: String?) {
        var frm: Fragment? = null
        when (tag) {
            FcmSocialLoginFrm.TAG -> frm = FcmSocialLoginFrm.getInstance(Bundle())
        }
        if (frm != null) navigateFragment(R.id.container, frm, false)
    }
}
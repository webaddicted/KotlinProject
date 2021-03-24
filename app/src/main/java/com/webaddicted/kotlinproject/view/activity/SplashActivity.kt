package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivitySplashBinding
import com.webaddicted.kotlinproject.global.common.AppApplication.Companion.context
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.global.constant.AppConstant
import com.webaddicted.kotlinproject.view.base.BaseActivity
import java.util.*


/**
 * Created by Deepak Sharma on 01/07/19.
 */
class SplashActivity : BaseActivity() {
    private lateinit var mBinding: ActivitySplashBinding

    companion object {
        val TAG: String = SplashActivity::class.java.simpleName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, SplashActivity::class.java))
        }

        fun newClearLogin(context: Activity?) {
            val intent = Intent(context, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
            context?.finish()
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun initUI(binding: ViewDataBinding) {
        mBinding = binding as ActivitySplashBinding
        init()
        setNavigationColor(ContextCompat.getColor(context, R.color.app_color))
        mBinding.imgLogo.setOnClickListener { init() }
    }

    private fun init() {
        val slideAnmimation = AnimationUtils.loadAnimation(this, R.anim.bounce_game)
        mBinding.imgLogo.animation = slideAnmimation
        navigateToNext()
    }

    /**
     * navigate to welcome activity after Splash timer Delay
     */
    private fun navigateToNext() {
        Handler(Looper.getMainLooper()).postDelayed({
            LanguageActivity.newIntent(this)
            finish()
        }, AppConstant.SPLASH_DELAY)
    }
}
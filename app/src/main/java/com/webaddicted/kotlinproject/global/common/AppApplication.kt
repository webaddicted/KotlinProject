package com.webaddicted.kotlinproject.global.common

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.global.koin.*
import com.webaddicted.kotlinproject.global.sharedpref.PreferenceUtils
import com.webaddicted.kotlinproject.global.sociallogin.SocialLogin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class AppApplication : Application() {
    private val mNetworkReceiver = NetworkChangeReceiver()

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        Stetho.initializeWithDefaults(this)
        FileHelper.createApplicationFolder()
        setupDefaultFont()
        PreferenceUtils.getInstance(this)
        SocialLogin.init(this)
        startKoin {
            androidLogger()
            androidContext(this@AppApplication)
            modules(getModule())
        }
        FirebaseApp.initializeApp(this)
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Lg.d("TAG", "okHttp new Token---> $refreshedToken")
        checkInternetConnection()
    }

    /**
     * set default font for app
     */
    private fun setupDefaultFont() {
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("font/opensans_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }

    private fun getModule(): List<Module> {
        return listOf(
            appModule,
            viewModelModule,
            repoModule,
            commonModelModule,
            dbModule
        )
    }

    private fun checkInternetConnection() {
        registerReceiver(mNetworkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}

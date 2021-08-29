package com.webaddicted.kotlinproject.global.sociallogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.global.sociallogin.auth.FacebookAuth
import com.webaddicted.kotlinproject.global.sociallogin.auth.GoogleAuth
import com.webaddicted.kotlinproject.global.sociallogin.auth.TwitterAuth
import com.webaddicted.kotlinproject.global.sociallogin.enumtype.LoginTypeEnum

/**
 * Created by Deepak Sharma(webaddicted) on 29/03/2020
 */
class SocialLogin {
    companion object {
        private var mAuth: FirebaseAuth? = null
        private var mLoginType: LoginTypeEnum? = null
        private var mContext: Context? = null

        /**
         * initialize Firebase
         */
        fun init(context: Context): FirebaseAuth? {
            mContext = context
            if (mAuth == null) {
                FirebaseApp.initializeApp(context)
                mAuth = FirebaseAuth.getInstance()
            }
            return mAuth
        }

        fun getFirebaseAuth(): FirebaseAuth? {
            return mAuth
        }

        /**
         * check user signIn status
         */
        fun isUserLogin(): Boolean {
            if (mAuth != null) {
                val currentUser = mAuth?.currentUser
                return currentUser != null
            }
            return false
        }

        fun loginType(loginType: LoginTypeEnum?) {
            mLoginType = loginType
        }

        /**
         * Check LoginType
         */
        fun getloginType(): LoginTypeEnum? {
            return mLoginType
        }

        fun getInstance(): Context? {
            return mContext
        }

        fun googleLogin(
            @NonNull activity: Activity,
            @NonNull clientId: String?,
            @NonNull loginResponse: OnSocialLoginListener?
        ){
            GoogleAuth.googleLogin(
                activity,
                clientId,
                loginResponse
            )
        }
        fun fbLogin(
            activity: Activity,
            loginResponse: OnSocialLoginListener?
        ) {
            FacebookAuth.fbLogin(activity, loginResponse)
        }
        fun twitterLogin(
            @NonNull activity: Activity,
            @NonNull twitter_consumer_key: String?,
            @NonNull twitter_consumer_secret: String?,
            @NonNull loginResponse: OnSocialLoginListener?
        ) {
            TwitterAuth.twitterLogin(
                activity,
                twitter_consumer_key,
                twitter_consumer_secret,
                loginResponse
            )
        }
        fun onActivityResult(
            activity: Activity?,
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ) {
            if (resultCode == Activity.RESULT_OK) {
                when (getloginType()) {
                    LoginTypeEnum.GOOGLE -> GoogleAuth.onActivityResult(
                        activity!!,
                        requestCode,
                        resultCode,
                        data
                    )
                    LoginTypeEnum.FACEBOOK -> FacebookAuth.onActivityResult(
                        requestCode,
                        resultCode,
                        data
                    )
                    LoginTypeEnum.TWITTER -> TwitterAuth.activityResult(
                        requestCode,
                        resultCode,
                        data
                    )
                }
            } else {
                Log.d("TAG", "onActivityResult: login Failed")
            }
        }

        fun logout(activity: Activity?) {
            GoogleAuth.logOut(activity)
            FacebookAuth.logOut()
            TwitterAuth.logOut(activity!!)
        }
    }
}

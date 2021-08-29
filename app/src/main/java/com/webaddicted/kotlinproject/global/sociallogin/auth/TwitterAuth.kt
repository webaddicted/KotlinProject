package com.webaddicted.kotlinproject.global.sociallogin.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import androidx.annotation.NonNull
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.TwitterAuthProvider
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.webaddicted.kotlinproject.global.sociallogin.OnSocialLoginListener
import com.webaddicted.kotlinproject.global.sociallogin.SocialLogin
import com.webaddicted.kotlinproject.global.sociallogin.enumtype.LoginTypeEnum
import com.webaddicted.kotlinproject.global.sociallogin.model.SocialLoginResponse

/**
 * Created by Deepak Sharma(webaddicted) on 29/03/2020
 */

class TwitterAuth {
    companion object {
        private val TAG = TwitterAuth::class.java.simpleName

        @NonNull
        private val mAuth: FirebaseAuth? = SocialLogin.getFirebaseAuth()
        private var mTwitterAuthClient: TwitterAuthClient? = null
        private var mLoginResponse: OnSocialLoginListener? = null

        /**
         * @param activity                referance of activity
         * @param loginResponse           user login listener
         * @param twitter_consumer_key    provided by twitter
         * @param twitter_consumer_secret is also provide by twitter
         */
        fun twitterLogin(
            @NonNull activity: Activity,
            @NonNull twitter_consumer_key: String?,
            @NonNull twitter_consumer_secret: String?,
            @NonNull loginResponse: OnSocialLoginListener?
        ) {
            SocialLogin.loginType(LoginTypeEnum.TWITTER)
            mLoginResponse = loginResponse
            val authConfig =
                TwitterAuthConfig(twitter_consumer_key, twitter_consumer_secret)
            val twitterConfig =
                TwitterConfig.Builder(activity).twitterAuthConfig(authConfig).build()
            Twitter.initialize(twitterConfig)
            mTwitterAuthClient = TwitterAuthClient()
            mTwitterAuthClient?.authorize(
                activity,
                object : Callback<TwitterSession>() {
                    override fun success(twitterSessionResult: Result<TwitterSession>) {
                        handleTwitterSession(activity, twitterSessionResult.data)
                    }

                    override fun failure(e: TwitterException) {
                        mLoginResponse?.onSocialLoginFailure("Twitter authentication failed " + e.message)
                        e.printStackTrace()
                    }
                })
        }

        /**
         * receive onActivityResult response
         */
        fun activityResult(
            requestCode: Int,
            @NonNull resultCode: Int,
            @NonNull data: Intent?
        ) {
            mTwitterAuthClient!!.onActivityResult(requestCode, resultCode, data)
        }

        /**
         * get user info from FCM
         *
         * @param activity referance of activity
         * @param session  get all user info.
         */
        private fun handleTwitterSession(
            @NonNull activity: Activity,
            @NonNull session: TwitterSession
        ) {
            val credential = TwitterAuthProvider.getCredential(
                session.authToken.token,
                session.authToken.secret
            )
            mAuth?.signInWithCredential(credential)?.addOnCompleteListener(
                activity
            ) { task ->
                if (task.isSuccessful) {
                    requestEmail(
                        task.result!!.user,
                        session.authToken.token
                    )
                    //                getUserInfo(mAuth.getCurrentUser());
                } else {
                    mLoginResponse?.onSocialLoginFailure("Twitter Authentication failed " + task.exception)
                    // If sign in fails, display a message to the user.
                }
            }
        }

        private fun requestEmail(userResult: FirebaseUser?, token: String) {
            val twitterSession =
                TwitterCore.getInstance().sessionManager.activeSession
            val profileImage = userResult!!.photoUrl.toString()
            val loginResponse = SocialLoginResponse().apply {
                userid = userResult.uid
                tokenId = token
                userName = userResult.displayName
                dob = ""
                userImage = profileImage
                provider = "Twitter"
            }
            mTwitterAuthClient!!.requestEmail(
                twitterSession,
                object : Callback<String>() {
                    override fun success(emailResult: Result<String>) {
                        val email = emailResult.data
                        loginResponse.userEmailId = email
                        mLoginResponse?.onSocialLoginSuccess(loginResponse)
                    }

                    override fun failure(e: TwitterException) {
                        mLoginResponse?.onSocialLoginSuccess(loginResponse)
                    }
                })
        }

        //    /**
        //     * @param firebaseUser is current login user
        //     */
        //    private static void getUserInfo(@NonNull FirebaseUser firebaseUser) {
        //        String strEmailId;
        //        String strPhoneNo;
        //        if (firebaseUser.getEmail() != null) strEmailId = firebaseUser.getEmail();
        //        else strEmailId = firebaseUser.getProviderData().get(0).getEmail();
        //
        //        if (firebaseUser.getPhoneNumber() != null) strPhoneNo = firebaseUser.getPhoneNumber();
        //        else strPhoneNo = firebaseUser.getProviderData().get(0).getPhoneNumber();
        //        LoginResponse loginResponse = new LoginResponse();
        //        loginResponse.setUserid(firebaseUser.getUid());
        //        loginResponse.setTokenId(firebaseUser.getIdToken(true).toString());
        //        loginResponse.setUserName(firebaseUser.getDisplayName());
        //        loginResponse.setUserEmailId(firebaseUser.getEmail());
        //        loginResponse.setDob("");
        //        loginResponse.setUserImage(firebaseUser.getPhotoUrl().toString());
        //        loginResponse.setProvider(firebaseUser.getProviderId());
        //        mLoginResponse.onSuccess(loginResponse);
        //    }
        fun logOut(mActivity: Activity) {
            try {
                val twitterSession =
                    TwitterCore.getInstance().sessionManager.activeSession
                if (twitterSession != null) {
                    clearTwitterCookies(mActivity)
                    //            Twitter.getSessionManager().clearActiveSession();
                    //            Twitter.logOut();

                }
            }catch (exp:Exception){
                exp.printStackTrace()
            }
        }

        private fun clearTwitterCookies(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                CookieManager.getInstance().removeAllCookies(null)
                CookieManager.getInstance().flush()
            } else {
                val cookieSyncMngr = CookieSyncManager.createInstance(context)
                cookieSyncMngr.startSync()
                val cookieManager =
                    CookieManager.getInstance()
                cookieManager.removeAllCookie()
                cookieManager.removeSessionCookie()
                cookieSyncMngr.stopSync()
                cookieSyncMngr.sync()
            }
        } //     public static boolean logOut() {
        //         CookieSyncManager.createInstance(SocialLogin.getInstance());
        //         CookieManager cookieManager = CookieManager.getInstance();
        //         cookieManager.removeSessionCookie();
        //         TwitterCore.getInstance().getSessionManager().clearActiveSession();
        //         return true;
        //     }
    }
}

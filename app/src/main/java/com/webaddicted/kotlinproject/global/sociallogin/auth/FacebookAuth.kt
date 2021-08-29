package com.webaddicted.kotlinproject.global.sociallogin.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.annotation.NonNull
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.GsonBuilder
import com.webaddicted.kotlinproject.global.sociallogin.OnSocialLoginListener
import com.webaddicted.kotlinproject.global.sociallogin.SocialLogin
import com.webaddicted.kotlinproject.global.sociallogin.enumtype.LoginTypeEnum
import com.webaddicted.kotlinproject.global.sociallogin.model.FbResponse
import com.webaddicted.kotlinproject.global.sociallogin.model.SocialLoginResponse
import org.json.JSONObject
import java.lang.reflect.Modifier
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by Deepak Sharma(webaddicted) on 29/03/2020
 */
class FacebookAuth {
    companion object {
        private val TAG = FacebookAuth::class.java.simpleName
        private val READ_PERMISSION =
            listOf(
                "public_profile",
                "email",
                "user_birthday",
                "user_photos"
            )
        private var callbackManager: CallbackManager? = null

        @NonNull
        private val mAuth: FirebaseAuth? = SocialLogin.getFirebaseAuth()
        private var mLoginResponse: OnSocialLoginListener? = null
        private var loginManager: LoginManager? = null

        /**
         * @param activity      referance of activity
         * @param loginResponse is describe user login status
         */
        fun fbLogin(
            activity: Activity,
            loginResponse: OnSocialLoginListener?
        ) {
            SocialLogin.loginType(LoginTypeEnum.FACEBOOK)
//          FacebookSdk.sdkInitialize(activity)
            loginManager = LoginManager.getInstance()
            mLoginResponse = loginResponse
            if (getAccessToken() == null) loginAuth(activity)
            else handleFacebookAccessToken(getAccessToken())
        }

        private fun loginAuth(activity: Activity) {
            loginManager!!.logInWithReadPermissions(
                activity,
                READ_PERMISSION
            )
            callbackManager = CallbackManager.Factory.create()
            loginManager!!.registerCallback(
                callbackManager, object :
                    FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        fcmAuth(activity, loginResult.accessToken)
                    }

                    override fun onCancel() {
                        mLoginResponse?.onSocialLoginFailure("Facebook Authentication failed.")
                    }

                    override fun onError(exception: FacebookException) {
                        exception.printStackTrace()
                        mLoginResponse?.onSocialLoginFailure("Facebook Authentication error " + exception.message)
                    }
                })
        }

        private fun fcmAuth(activity: Activity, accessToken: AccessToken) {
            val credential = FacebookAuthProvider.getCredential(accessToken.token)
            mAuth?.signInWithCredential(credential)?.addOnCompleteListener(
                activity
            ) { task: Task<AuthResult?> ->
                if (task.isSuccessful) handleFacebookAccessToken(accessToken)
                else
                    mLoginResponse?.onSocialLoginFailure("Authentication failed : ${task.exception}")

            }
        }

        /**
         * Receive onActivityResult response
         * @param requestCode
         * @param resultCode
         * @param data
         */
        fun onActivityResult(
            @NonNull requestCode: Int,
            @NonNull resultCode: Int,
            @NonNull data: Intent?
        ) {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }

        /**
         * Get user info from fb token
         * @param token fb token
         */
        private fun handleFacebookAccessToken(@NonNull token: AccessToken?) {
            val request =
                GraphRequest.newMeRequest(token) { jsonObj: JSONObject, response: GraphResponse? ->
                    val fbResponse: FbResponse =
                        fromJson(jsonObj.toString(), FbResponse::class.java)
                    Log.d(
                        TAG,
                        """onCompleted: str_facebookname -> ${fbResponse.name.toString()}
 str_facebookemail -> ${fbResponse.email.toString()}
 str_facebookid -> ${fbResponse.id.toString()}
 str_birthday -> ${fbResponse.birthday.toString()}
 strPhoto -> """ + fbResponse.picture?.data?.url
                            .toString() +
                                "\n token-> " + token!!.token
                    )
                    getUserInfo(
                        fbResponse,
                        mAuth?.currentUser,
                        token.token
                    )
                }
            val parameters = Bundle()
            parameters.putString(
                "fields",
                "id,name,first_name,last_name,gender,birthday,email,cover,picture.type(large),photos"
            )
            request.parameters = parameters
            request.executeAsync()
        }

        private fun <T> fromJson(json: String?, clazz: Class<T>?): T {
            return GsonBuilder()
                .excludeFieldsWithModifiers(
                    Modifier.FINAL,
                    Modifier.TRANSIENT,
                    Modifier.STATIC
                )
                .create().fromJson(json, clazz)
        }

        /**
         * @param fbResponse
         * @param firebaseUser is current login user
         * @param token
         */
        private fun getUserInfo(
            fbResponse: FbResponse,
            @NonNull firebaseUser: FirebaseUser?,
            token: String
        ) {
            val loginResponse = SocialLoginResponse()
            loginResponse.userid = firebaseUser?.uid
            loginResponse.tokenId = token
            loginResponse.userName = fbResponse.name
            loginResponse.userEmailId = fbResponse.email
            loginResponse.dob = fbResponse.birthday
            loginResponse.userImage = fbResponse.picture?.data?.url
            loginResponse.provider = "Facebook"
            mLoginResponse?.onSocialLoginSuccess(loginResponse)
        }

        /**
         * get keyhash for facebook
         *
         * @param context referance of activity
         */
        fun getHashKey(@NonNull context: Context): String? {
            var keyhash: String? = null
            try {
                val info = context.packageManager
                    .getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    keyhash =
                        Base64.encodeToString(md.digest(), Base64.DEFAULT)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return keyhash
        }

        fun logOut(): Boolean {
            try {
                FirebaseAuth.getInstance().signOut()
                LoginManager.getInstance().logOut()
            } catch (exp: Exception) {
                exp.printStackTrace()
            }
            return true
        }

        fun isLoggedIn(): Boolean {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null
        }

        private fun getAccessToken(): AccessToken? {
            return AccessToken.getCurrentAccessToken()
        }
    }
}

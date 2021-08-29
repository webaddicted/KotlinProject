package com.webaddicted.kotlinproject.global.sociallogin.auth

import android.app.Activity
import android.content.Intent
import androidx.annotation.NonNull
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.webaddicted.kotlinproject.global.sociallogin.OnSocialLoginListener
import com.webaddicted.kotlinproject.global.sociallogin.SocialLogin
import com.webaddicted.kotlinproject.global.sociallogin.enumtype.LoginTypeEnum
import com.webaddicted.kotlinproject.global.sociallogin.model.SocialLoginResponse

/**
 * Created by Deepak Sharma(webaddicted) on 29/03/2020
 */

class GoogleAuth {
    companion object {
        private val TAG = GoogleAuth::class.java.simpleName
        private var mGoogleSignInClient: GoogleSignInClient? = null

        @NonNull
        private val mAuth: FirebaseAuth? = SocialLogin.getFirebaseAuth()
        private var mLoginResponse: OnSocialLoginListener? = null

        /**
         * @param activity      referance of activity
         * @param loginResponse is describe user login status
         * @param clientId      is default_web_client_id
         */
        fun googleLogin(
            @NonNull activity: Activity,
            @NonNull clientId: String?,
            @NonNull loginResponse: OnSocialLoginListener?
        ) {
            mLoginResponse = loginResponse
            SocialLogin.loginType(
                LoginTypeEnum.GOOGLE
            )
            if (mAuth != null) {
                if (mAuth.currentUser == null) {
                    val gso = GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN
                    )
                        .requestIdToken(clientId)
                        .requestEmail()
                        .build()
                    mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
                    val signInIntent = mGoogleSignInClient?.signInIntent
                    activity.startActivityForResult(
                        signInIntent,
                        RC_SIGN_IN
                    )
                } else {
                    getUserInfo(
                        mAuth.currentUser,
                        GoogleSignIn.getLastSignedInAccount(activity)
                    )
                }
            }
        }

        /**
         * @param activity    referance of activity
         * @param requestCode is google sigin login request.
         * @param resultCode  is RESULT_OK
         * @param data        have google signin information
         */
        fun onActivityResult(
            @NonNull activity: Activity,
            @NonNull requestCode: Int,
            @NonNull resultCode: Int,
            @NonNull data: Intent?
        ) {
            if (requestCode == RC_SIGN_IN) {
                val task =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(
                        ApiException::class.java
                    )
                    firebaseAuthWithGoogle(
                        activity,
                        account
                    )
                } catch (e: ApiException) {
                    e.printStackTrace()
                    mLoginResponse?.onSocialLoginFailure("Authentication failed : $e")
                }
            } else {
                mLoginResponse?.onSocialLoginFailure("Authentication failed.")
            }
        }

        /**
         * get user info from FCM
         *
         * @param activity referance of activity
         * @param account  is contain all user info.
         */
        private fun firebaseAuthWithGoogle(
            @NonNull activity: Activity,
            @NonNull account: GoogleSignInAccount?
        ) {
            //getting the auth credential
            val credential =
                GoogleAuthProvider.getCredential(account!!.idToken, null)
            //save usr info on firebase
            mAuth!!.signInWithCredential(credential).addOnCompleteListener(
                activity
            ) { task ->
                if (task.isSuccessful) {
                    getUserInfo(
                        mAuth.currentUser,
                        account
                    )
                } else {
                    mLoginResponse?.onSocialLoginFailure("SignIn Authentication failed.")
                }
            }
        }

        /**
         * @param firebaseUser is current login user
         * @param account
         */
        private fun getUserInfo(
            @NonNull firebaseUser: FirebaseUser?,
            account: GoogleSignInAccount?
        ) {
            val loginResponse = SocialLoginResponse().apply {
                userid = firebaseUser?.uid
                tokenId = account?.idToken
                userName = account?.displayName
                userEmailId = account?.email
                dob = ""
                userImage = account?.photoUrl.toString()
                provider = "Google"
            }
            mLoginResponse?.onSocialLoginSuccess(loginResponse)
        }

        fun isLogin(activity: Activity?): GoogleSignInClient {
            val gso = GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            )
                .requestEmail()
                .build()
            return GoogleSignIn.getClient(activity!!, gso)
        }

        fun logOut(activity: Activity?): Boolean {
            try {
                FirebaseAuth.getInstance().signOut()
                isLogin(
                    activity
                ).signOut()
            } catch (exp: Exception) {
                exp.printStackTrace()
            }
            return true
        }

        const val RC_SIGN_IN = 64206
    }
}

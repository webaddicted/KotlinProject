package com.webaddicted.kotlinproject.view.fcmkit

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
//import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.apiutils.ApiConstant.Companion.FCM_DB_USERS
import com.webaddicted.kotlinproject.apiutils.ApiConstant.Companion.FCM_USERS_EMAIL_ID
import com.webaddicted.kotlinproject.databinding.FrmFcmSocialLoginBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.global.common.PermissionHelper
import com.webaddicted.kotlinproject.global.sociallogin.OnSocialLoginListener
import com.webaddicted.kotlinproject.global.sociallogin.SocialLogin
import com.webaddicted.kotlinproject.global.sociallogin.enumtype.LoginTypeEnum
import com.webaddicted.kotlinproject.global.sociallogin.model.SocialLoginResponse
import com.webaddicted.kotlinproject.model.fcmkit.FcmSocialLoginRespoBean
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.viewModel.fcmkit.FcmFoodViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FcmSocialLoginFrm : BaseFragment(), OnSocialLoginListener {
    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var mBinding: FrmFcmSocialLoginBinding
    private val viewModel: FcmFoodViewModel by viewModel()

    companion object {
        val TAG = FcmSocialLoginFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): FcmSocialLoginFrm {
            val fragment = FcmSocialLoginFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_fcm_social_login
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmFcmSocialLoginBinding
        init()
        clickListener()
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
        callbackManager = CallbackManager.Factory.create()
    }


    private fun clickListener() {
        mBinding.btnGoogle.setOnClickListener(this)
        mBinding.btnFb.setOnClickListener(this)
        mBinding.btnTwitter.setOnClickListener(this)
        mBinding.btnLogin.setOnClickListener(this)
        mBinding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_google -> checkReadPhoneStatePerm(LoginTypeEnum.GOOGLE)
            R.id.btn_fb -> checkReadPhoneStatePerm(LoginTypeEnum.FACEBOOK)
            R.id.btn_twitter -> checkReadPhoneStatePerm(LoginTypeEnum.TWITTER)
            R.id.btn_login -> navigateScreen(FcmLoginFrm.TAG, Bundle())
            R.id.btn_register -> checkReadPhoneStatePerm(LoginTypeEnum.MANUAL_LOGIN)
        }
    }

    private fun checkReadPhoneStatePerm(loginTypeEnum: LoginTypeEnum) {
        val multiplePermission = ArrayList<String>()
        multiplePermission.add(Manifest.permission.READ_PHONE_STATE)
        activity?.let {
            PermissionHelper.requestMultiplePermission(
                it,
                multiplePermission,
                object : PermissionHelper.Companion.PermissionListener {
                    override fun onPermissionGranted(mCustomPermission: List<String>) {
                        inspectClick(loginTypeEnum)
                    }

                    override fun onPermissionDenied(mCustomPermission: List<String>) {

                    }

                })
        }
    }

    private fun inspectClick(loginTypeEnum: LoginTypeEnum) {
        when (loginTypeEnum) {
            LoginTypeEnum.GOOGLE -> {
                SocialLogin.logout(activity)
//                activity?.let {
//                    SocialLogin.googleLogin(
//                        it,
//                        getString(R.string.default_web_client_id),
//                        this
//                    )
//                }
            }
            LoginTypeEnum.FACEBOOK -> {
                SocialLogin.logout(activity)
                activity?.let { SocialLogin.fbLogin(it, this) }
            }
            LoginTypeEnum.TWITTER -> {
                SocialLogin.logout(activity)
                activity?.let {
                    SocialLogin.twitterLogin(
                        it,
                        getString(R.string.twitter_consumer_key),
                        getString(R.string.twitter_consumer_secret),
                        this
                    )
                }
            }
            LoginTypeEnum.MANUAL_LOGIN -> navigateScreen(FcmSignupFrm.TAG, Bundle())
        }
    }


    override fun onSocialLoginSuccess(loginResponse: SocialLoginResponse?) {
        checkUserExist(loginResponse)
        Lg.d(TAG, "Social login : " + Gson().toJson(loginResponse))
    }

    override fun onSocialLoginSuccess(success: String?) {
        GlobalUtility.showToast(success!!)
        Lg.d(TAG, success)
    }

    override fun onSocialLoginFailure(failure: String?) {
        GlobalUtility.showToast(failure!!)
        Lg.d(TAG, failure)
    }

    private fun checkUserExist(loginResponse: SocialLoginResponse?) {
        val dbRef = getFcmDBRef(FCM_DB_USERS)
        val query = dbRef.orderByChild(FCM_USERS_EMAIL_ID).equalTo(loginResponse?.userEmailId)
        showApiLoader()
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                hideApiLoader()
                dbRef.removeEventListener(this)
                if (dataSnapshot.value != null) {
                    for (noteDataSnapshot in dataSnapshot.children) {
                        val note = noteDataSnapshot.getValue(FcmSocialLoginRespoBean::class.java)
                        viewModel.setFcmFoodUserInfo(note!!)
                        break
                    }
                    FcmFoodHomeActivity.newIntent(requireActivity())
                } else {
                    val respo = FcmSocialLoginRespoBean().apply {
                        uuid = loginResponse?.userid
                        imei = GlobalUtility.getDeviceIMEI(requireActivity())
                        tokenId = loginResponse?.tokenId
                        userName = loginResponse?.userName
                        userEmailId = loginResponse?.userEmailId
                        userMobileno = loginResponse?.userMobileno
                        userImage = loginResponse?.userImage
                        dob = loginResponse?.dob
                        provider = loginResponse?.provider
//                        fcmToken = FirebaseInstanceId.getInstance().token
                    }
                    val bundle = Bundle()
                    bundle.putSerializable(FcmSignupFrm.SOCIAL_RESPO, respo)
                    navigateScreen(FcmSignupFrm.TAG, bundle)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                hideApiLoader()
                GlobalUtility.showToast(databaseError.message)
                dbRef.removeEventListener(this)
            }
        })
    }

    /**
     * navigate on fragment
     *
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String, bundle: Bundle) {
        var frm: Fragment? = null
        when (tag) {
            FcmSignupFrm.TAG -> frm = FcmSignupFrm.getInstance(bundle)
            FcmLoginFrm.TAG -> frm = FcmLoginFrm.getInstance(Bundle())
            FcmOtpFrm.TAG -> frm = FcmOtpFrm.getInstance(Bundle())
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }
}


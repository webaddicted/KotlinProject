package com.webaddicted.kotlinproject.view.fcmkit

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.apiutils.ApiConstant
import com.webaddicted.kotlinproject.apiutils.ApiConstant.Companion.FCM_STORAGE_PROFILE
import com.webaddicted.kotlinproject.apiutils.ApiConstant.Companion.PHONE_AUTH_TIMEOUT
import com.webaddicted.kotlinproject.databinding.FrmFcmOtpBinding
import com.webaddicted.kotlinproject.global.common.*
import com.webaddicted.kotlinproject.global.sociallogin.SocialLogin
import com.webaddicted.kotlinproject.model.fcmkit.FcmSocialLoginRespoBean
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.util.concurrent.TimeUnit


class FcmOtpFrm : BaseFragment(R.layout.frm_fcm_otp) {
    private var userVerified: Boolean = false
    private var openFrom: String? = ""
    private var mobileNo: String? = ""
    private var mResendToken: ForceResendingToken? = null
    private var mAuth: FirebaseAuth? = null
    private var mCallbacks: OnVerificationStateChangedCallbacks? = null
    private var socialLoginRespo: FcmSocialLoginRespoBean? = null
    private lateinit var mBinding: FrmFcmOtpBinding
    private var checkOtpVerifyFirstTime = true
    private var mVerificationId: String? = null

    companion object {
        val TAG = FcmOtpFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): FcmOtpFrm {
            val fragment = FcmOtpFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmFcmOtpBinding
        init()
        clickListener()
    }

    private fun init() {
        SocialLogin.logout(activity)
        mAuth = FirebaseAuth.getInstance()

        if (arguments != null && arguments?.containsKey(FcmSignupFrm.SOCIAL_RESPO)!!) {
            socialLoginRespo =
                arguments?.getSerializable(FcmSignupFrm.SOCIAL_RESPO) as FcmSocialLoginRespoBean
            mBinding.wrapperPwd.gone()
            mobileNo = "+91" + socialLoginRespo?.userMobileno
            phoneAuth()
            startPhoneNumberVerification(mobileNo!!)
        }
        if (arguments != null && arguments?.containsKey(FcmSignupFrm.OPEN_FROM)!!) {
            openFrom = arguments?.getString(FcmSignupFrm.OPEN_FROM)
            if (openFrom.equals(FcmForgotPassFrm.TAG)) {
                mBinding.wrapperPwd.visible()
            }
        }
    }

    private fun clickListener() {
        mBinding.includeBack.linearBack.setOnClickListener(this)
        mBinding.btnVerify.setOnClickListener(this)
        mBinding.txtResendOtp.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.include_back -> requireActivity().onBackPressedDispatcher.onBackPressed()
            R.id.txt_resend_otp -> {
                if (checkOtpVerifyFirstTime) startPhoneNumberVerification(mobileNo!!)
                else resendVerificationCode(mobileNo!!, mResendToken!!)
            }
            R.id.btn_verify -> validate()
        }
    }

    private fun phoneAuth() {
        mCallbacks = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted : $credential")
                mBinding.edtOtp.setText(credential.smsCode)
//                mBinding.txtResendOtp.isClickable = false
                userVerified = true
//                GlobalUtility.setEnableView(mBinding.edtOtp, false)
                if (openFrom.equals(FcmForgotPassFrm.TAG))
                    mBinding.wrapperOtp.gone()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed $e")
                if (e is FirebaseAuthInvalidCredentialsException) {
                    GlobalUtility.showToast("Invalid phone number.")
                } else if (e is FirebaseTooManyRequestsException) {
                    GlobalUtility.showToast("Quota exceeded.")
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent : $verificationId")
                mVerificationId = verificationId
                mResendToken = token
            }
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,  // Phone number to verify
            PHONE_AUTH_TIMEOUT,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            mActivity,  // Activity (for callback binding)
            mCallbacks!!
        )
        checkOtpVerifyFirstTime = false
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: ForceResendingToken
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,  // Phone number to verify
            PHONE_AUTH_TIMEOUT,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            mActivity,  // Activity (for callback binding)
            mCallbacks!!,  // OnVerificationStateChangedCallbacks
            token
        ) // ForceResendingToken from callbacks
    }

    private fun validate() {
        if (mBinding.wrapperOtp.visibility == View.VISIBLE && !ValidationHelper.validateOTPCode(
                mBinding.edtOtp,
                mBinding.wrapperOtp
            ) ||
            (mBinding.wrapperPwd.visibility == View.VISIBLE && !ValidationHelper.validatePwd(
                mBinding.edtPwd,
                mBinding.wrapperPwd
            ))
        ) return
        if (userVerified) verifyUser()
        else
            verifyPhoneNumberWithCode(mVerificationId!!, mBinding.edtOtp.text.toString())
    }

    private fun verifyPhoneNumberWithCode(
        verificationId: String,
        code: String
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(mActivity) { task ->
            if (task.isSuccessful) {
                val user = task.result.user
                if (openFrom.equals(FcmForgotPassFrm.TAG)) validate()
                else verifyUser()
            } else {
//                verifyUser()
                TAG?.let { Lg.e(it, "signInWithCredential:failure : ${task.exception}") }
                if (task.exception is FirebaseAuthInvalidCredentialsException)
                    GlobalUtility.showToast("Invalid code.")
                else GlobalUtility.showToast(task.exception?.message!!)
            }
        }
    }

    private fun verifyUser() {
        val dbRef = getFcmDBRef(ApiConstant.FCM_DB_USERS)
        if (openFrom.equals(FcmForgotPassFrm.TAG)) {
            dbRef.child(socialLoginRespo?.userMobileno!!).child(ApiConstant.FCM_USERS_PASSWORD)
                .setValue(mBinding.edtPwd.text.toString())
            FcmFoodActivity.newIntent(mActivity)
        } else if (socialLoginRespo?.profileImgFile != null && socialLoginRespo?.profileImgFile?.exists()!!) {
            uploadImage(dbRef)
        } else {
            insertUser(dbRef)
        }
    }

    private fun insertUser(dbRef: DatabaseReference) {
        dbRef.child(socialLoginRespo?.userMobileno!!).setValue(socialLoginRespo)
//        databaseReference.push()
        GlobalUtility.showToast(getString(R.string.account_successfully_created))
        FcmFoodActivity.newIntent(mActivity)
    }

    private fun uploadImage(dbRef: DatabaseReference) {
        val storeImage: StorageReference = getFcmStorageRef(
            FCM_STORAGE_PROFILE + "${socialLoginRespo?.userMobileno}_${System.currentTimeMillis()}" + ".jpg"
        )
        storeImage.putFile(Uri.fromFile(socialLoginRespo?.profileImgFile)).addOnSuccessListener {
            storeImage.downloadUrl.addOnSuccessListener { uri ->
                socialLoginRespo?.userImage = uri.toString()
                socialLoginRespo?.profileImgFile = null
                insertUser(dbRef)
            }
        }.addOnFailureListener { e ->
            GlobalUtility.showToast("Uploading failed " + e.message)
            TAG?.let { Lg.d(it, "onFailure: Uploading failed : ${e.message}") }
        }.addOnProgressListener { taskSnapshot ->
            val doubb =
                100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
            TAG?.let { Lg.d(it, "Uploading  : $doubb%") }
//            GlobalUtility.showToast("Upload : $doubb%")
        }
    }

    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            FcmLoginFrm.TAG -> frm = FcmLoginFrm.getInstance(Bundle())
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }

    override fun onResume() {
        super.onResume()
        addBlankSpace(mBinding.bottomSpace)
    }
}


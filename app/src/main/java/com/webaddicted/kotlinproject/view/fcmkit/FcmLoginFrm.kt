package com.webaddicted.kotlinproject.view.fcmkit

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
//import com.google.firebase.iid.FirebaseInstanceId
import com.webaddicted.kotlinproject.BuildConfig
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.apiutils.ApiConstant
import com.webaddicted.kotlinproject.databinding.FrmFcmLoginBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.ValidationHelper
import com.webaddicted.kotlinproject.model.fcmkit.FcmSocialLoginRespoBean
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.viewModel.fcmkit.FcmFoodViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FcmLoginFrm : BaseFragment() {
    private lateinit var mBinding: FrmFcmLoginBinding
    private val viewModel: FcmFoodViewModel by viewModel()

    companion object {
        val TAG = FcmLoginFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): FcmLoginFrm {
            val fragment = FcmLoginFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_fcm_login
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmFcmLoginBinding
        init()
        clickListener()
    }

    private fun init() {
        if (!BuildConfig.IS_UAT) {
            mBinding.edtMobile.setText("9024061407")
            mBinding.edtPwd.setText("India12345")
        }
    }

    private fun clickListener() {
        mBinding.btnLogin.setOnClickListener(this)
        mBinding.txtForgotPass.setOnClickListener(this)
        mBinding.txtSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_login -> validate()
            R.id.txt_forgot_pass -> navigateScreen(FcmForgotPassFrm.TAG)
            R.id.txt_sign_up -> navigateScreen(FcmSignupFrm.TAG)
        }
    }

    private fun validate() {
        if (!ValidationHelper.validateMobileNo(mBinding.edtMobile, mBinding.wrapperMobile) ||
            !ValidationHelper.validatePwd(mBinding.edtPwd, mBinding.wrapperPwd)
        ) return
        apiLogin()
    }

    private fun apiLogin() {
        val dbRef = getFcmDBRef(ApiConstant.FCM_DB_USERS)
        val query = dbRef.orderByChild(ApiConstant.FCM_USERS_MOBILE_NO)
            .equalTo(mBinding.edtMobile.text.toString())
        showApiLoader()
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                hideApiLoader()
                dbRef.removeEventListener(this)
                if (dataSnapshot.value != null) {

                    for (noteDataSnapshot in dataSnapshot.children) {
                        val note = noteDataSnapshot.getValue(FcmSocialLoginRespoBean::class.java)
//                        dbRef.child(note?.userMobileno!!).child(ApiConstant.FCM_USERS_FCM_TOKEN)
//                            .setValue(FirebaseInstanceId.getInstance().token)
//                        note.fcmToken = FirebaseInstanceId.getInstance().token
//                        viewModel.setFcmFoodUserInfo(note)
                        break
                    }
                    FcmFoodHomeActivity.newIntent(activity!!)
                } else
                    GlobalUtility.showToast(getString(R.string.account_does_not_exist))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                hideApiLoader()
                GlobalUtility.showToast(databaseError.message)
                dbRef.removeEventListener(this)
            }
        })

    }

    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            FcmOtpFrm.TAG -> frm = FcmOtpFrm.getInstance(Bundle())
            FcmForgotPassFrm.TAG -> frm = FcmForgotPassFrm.getInstance(Bundle())
            FcmSignupFrm.TAG -> frm = FcmSignupFrm.getInstance(Bundle())
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }

    override fun onResume() {
        super.onResume()
        addBlankSpace(mBinding.bottomSpace)
    }
}


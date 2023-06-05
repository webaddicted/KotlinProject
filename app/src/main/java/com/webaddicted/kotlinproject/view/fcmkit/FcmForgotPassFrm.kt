package com.webaddicted.kotlinproject.view.fcmkit

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.webaddicted.kotlinproject.BuildConfig
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.apiutils.ApiConstant
import com.webaddicted.kotlinproject.databinding.FrmFcmForgotPassBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.ValidationHelper
import com.webaddicted.kotlinproject.model.fcmkit.FcmSocialLoginRespoBean
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.fcmkit.FcmSignupFrm.Companion.OPEN_FROM
import com.webaddicted.kotlinproject.viewModel.fcmkit.FcmFoodViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FcmForgotPassFrm : BaseFragment(R.layout.frm_fcm_forgot_pass) {
    private lateinit var mBinding: FrmFcmForgotPassBinding
    private val viewModel: FcmFoodViewModel by viewModel()

    companion object {
        val TAG = FcmForgotPassFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): FcmForgotPassFrm {
            val fragment = FcmForgotPassFrm()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmFcmForgotPassBinding
        init()
        clickListener()
    }

    private fun init() {
        if (!BuildConfig.IS_UAT) {
            mBinding.edtMobile.setText("9024061407")
        }
    }

    private fun clickListener() {
        mBinding.includeBack.linearBack.setOnClickListener(this)
        mBinding.btnForgot.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.include_back -> requireActivity().onBackPressedDispatcher.onBackPressed()
            R.id.btn_forgot -> validate()
        }
    }

    private fun validate() {
        if (!ValidationHelper.validateMobileNo(mBinding.edtMobile, mBinding.wrapperMobile)) return
        apiForgotPass()
    }

    private fun apiForgotPass() {
        val dbRef = getFcmDBRef(ApiConstant.FCM_DB_USERS)
        val query = dbRef.orderByChild(ApiConstant.FCM_USERS_MOBILE_NO)
            .equalTo(mBinding.edtMobile.text.toString())
        showApiLoader()
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                hideApiLoader()
                dbRef.removeEventListener(this)
                if (dataSnapshot.value != null) {
                    var userInfoBean: FcmSocialLoginRespoBean? = null
                    for (noteDataSnapshot in dataSnapshot.children) {
                        userInfoBean =
                            noteDataSnapshot.getValue(FcmSocialLoginRespoBean::class.java)
                        break
                    }
                    val bundle = Bundle()
                    bundle.putSerializable(FcmSignupFrm.SOCIAL_RESPO, userInfoBean)
                    bundle.putString(OPEN_FROM, TAG)
                    navigateScreen(FcmOtpFrm.TAG, bundle)
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

    private fun navigateScreen(tag: String?, bundle: Bundle) {
        var frm: Fragment? = null
        when (tag) {
            FcmOtpFrm.TAG -> frm = FcmOtpFrm.getInstance(bundle)
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }

    override fun onResume() {
        super.onResume()
        addBlankSpace(mBinding.bottomSpace)
    }
}


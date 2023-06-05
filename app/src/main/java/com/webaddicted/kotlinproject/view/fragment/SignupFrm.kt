package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmSignupBinding
import com.webaddicted.kotlinproject.global.common.ValidationHelper
import com.webaddicted.kotlinproject.global.db.entity.UserInfoEntity
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.viewModel.common.CommonViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignupFrm : BaseFragment(R.layout.frm_signup) {
    private lateinit var mBinding: FrmSignupBinding
    private val commonViewModel: CommonViewModel by viewModel()

    companion object {
        val TAG = SignupFrm::class.qualifiedName

        fun getInstance(bundle: Bundle): SignupFrm {
            val fragment = SignupFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmSignupBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.edtFullName.setText("deepak sharma")
        mBinding.edtNickName.setText("namesr")
        mBinding.edtMobileNo.setText("9950607002")
        mBinding.edtEmail.setText("deepak@gmail.com")
        mBinding.edtPassword.setText("Test@12345")
    }

    private fun clickListener() {
        mBinding.btnLogin.setOnClickListener(this)
        mBinding.btnSignup.setOnClickListener(this)

//        mBinding.txtForgotPsw.setOnClickListener { this }
    }

    override fun onResume() {
        super.onResume()
        addBlankSpace(mBinding.bottomSpace)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_login -> requireActivity().onBackPressedDispatcher.onBackPressed()
            R.id.btn_signup -> validate()
        }
    }

    private fun validate() {

        if (ValidationHelper.validateBlank(
                mBinding.edtFullName,
                mBinding.wrapperFullName,
                getString(R.string.first_name_can_not_blank)
            ) &&
            ValidationHelper.validateName(mBinding.edtNickName, mBinding.wrapperNickName) &&
            ValidationHelper.validateMobileNo(mBinding.edtMobileNo, mBinding.wrapperMobileNo) &&
            ValidationHelper.validateEmail(mBinding.edtEmail, mBinding.wrapperEmail) &&
            ValidationHelper.validatePwd(mBinding.edtPassword, mBinding.wrapperPassword)
        ) {
            val user = UserInfoEntity()
            user.name = mBinding.edtFullName.text.toString().trim()
            user.nickname = mBinding.edtNickName.text.toString().trim()
            user.mobileno = mBinding.edtMobileNo.text.toString().trim()
            user.email = mBinding.edtEmail.text.toString().trim()
            user.password = mBinding.edtPassword.text.toString().trim()
            commonViewModel.insertUser(user)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * navigate on fragment
     *
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            LoginFrm.TAG -> frm = LoginFrm.getInstance(Bundle())
        }
        if (frm != null) navigateFragment(R.id.container, frm, false)
    }


}

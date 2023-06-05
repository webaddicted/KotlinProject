package com.webaddicted.kotlinproject.view.ecommerce

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmEcomOtpBinding
import com.webaddicted.kotlinproject.global.common.ValidationHelper
import com.webaddicted.kotlinproject.global.common.showToast
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.fragment.SignupFrm

class EcommOtpFrm : BaseFragment(R.layout.frm_ecom_otp) {
    private lateinit var mBinding: FrmEcomOtpBinding

    companion object {
        val TAG = EcommOtpFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): EcommOtpFrm {
            val fragment = EcommOtpFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmEcomOtpBinding
        init()
        clickListener()
    }

    private fun init() {
    }

    private fun clickListener() {
        mBinding.imgNext.setOnClickListener(this)
        mBinding.txtResendOTP.setOnClickListener(this)
        mBinding.txtEditMobileNo.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_next -> validate()
            R.id.txt_edit_mobile_no -> requireActivity().onBackPressedDispatcher.onBackPressed()
            R.id.txt_ResendOTP -> activity?.showToast(getString(R.string.otp_send_success))
        }
    }

    private fun validate() {
        if (ValidationHelper.validateCode(mBinding.edtOtp))
            EcommHomeActivity.newIntent(mActivity)
    }

    override fun onResume() {
        super.onResume()
        addBlankSpace(mBinding.bottomSpace)
    }
    /**
     * navigate on fragment
     *
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            SignupFrm.TAG -> frm = SignupFrm.getInstance(Bundle())
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }

}


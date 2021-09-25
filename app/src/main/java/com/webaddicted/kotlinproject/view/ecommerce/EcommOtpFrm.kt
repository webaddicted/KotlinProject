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

class EcommOtpFrm : BaseFragment() {
    private lateinit var mBinding: FrmEcomOtpBinding

    companion object {
        val TAG = EcommOtpFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): EcommOtpFrm {
            val fragment = EcommOtpFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_ecom_otp
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
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
            R.id.txt_edit_mobile_no -> activity?.onBackPressed()
            R.id.txt_ResendOTP -> activity?.showToast(getString(R.string.otp_send_success))
        }
    }

    private fun validate() {
        if (ValidationHelper.validateCode(mBinding.edtOtp))
            EcommHomeActivity.newIntent(requireActivity())
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


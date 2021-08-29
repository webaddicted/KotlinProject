package com.webaddicted.kotlinproject.view.ecommerce

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmEcomLoginBinding
import com.webaddicted.kotlinproject.global.common.ValidationHelper
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.fragment.SignupFrm

class EcommLoginFrm : BaseFragment() {
    private lateinit var mBinding: FrmEcomLoginBinding

    companion object {
        val TAG = EcommLoginFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): EcommLoginFrm {
            val fragment = EcommLoginFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_ecom_login
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmEcomLoginBinding
        init()
        clickListener()
    }

    private fun init() {
    }

    private fun clickListener() {
        mBinding.imgNext.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_next -> validate()
        }
    }

    private fun validate() {
        if (ValidationHelper.validateMobileNo(mBinding.edtMobileNo))
            navigateScreen(EcommOtpFrm.TAG)
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
            EcommOtpFrm.TAG -> frm = EcommOtpFrm.getInstance(Bundle())
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }

}


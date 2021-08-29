package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.github.gcacace.signaturepad.views.SignaturePad
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmDigitalSignatureBinding
import com.webaddicted.kotlinproject.global.common.FileHelper
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment


class DigitalSignatureFrm : BaseFragment() {
    private lateinit var mBinding: FrmDigitalSignatureBinding

    companion object {
        val TAG = DigitalSignatureFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): DigitalSignatureFrm {
            val fragment = DigitalSignatureFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_digital_signature
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmDigitalSignatureBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.digital_signature)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnSave.setOnClickListener(this)
        mBinding.btnClear.setOnClickListener(this)
        mBinding.signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {

            }

            override fun onSigned() {
                //Event triggered when the pad is signed
            }

            override fun onClear() {
                GlobalUtility.showToast(getString(R.string.clear))
            }
        })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_clear -> mBinding.signaturePad.clearView()
            R.id.btn_save -> {
                val bitmap = mBinding.signaturePad.signatureBitmap
                FileHelper.saveBitmapImg(bitmap, "Signature_${System.currentTimeMillis()}.jpeg")
            }
        }
    }

}


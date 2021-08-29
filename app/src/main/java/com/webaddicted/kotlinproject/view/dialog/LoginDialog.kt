package com.webaddicted.kotlinproject.view.dialog

import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.DialogCustomBinding
import com.webaddicted.kotlinproject.global.common.DialogUtil
import com.webaddicted.kotlinproject.global.common.showToast
import com.webaddicted.kotlinproject.view.base.BaseDialog


class LoginDialog : BaseDialog() {
    private lateinit var mBinding: DialogCustomBinding
    companion object {
        val TAG = LoginDialog::class.java.simpleName
        fun dialog(
        ): LoginDialog {
            val customDialog = LoginDialog()
            return customDialog
        }
    }

    override fun getLayout(): Int {
        return R.layout.dialog_custom
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as DialogCustomBinding
        init()
        clickListener()
    }

    /**
     * initialize view
     */
    private fun init() {
    }

    /**
     * widget click listener
     */
    private fun clickListener() {
        mBinding.btnOk.setOnClickListener(this)
        mBinding.txtCancel.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        dialog?.let { DialogUtil.fullScreenDialogBound(it) }
    }

   override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_ok -> {
                activity?.showToast("ok click")
                dismiss()
            }
            R.id.txt_cancel -> {
                activity?.showToast("cancel click")
                dismiss()
            }
        }
    }
}

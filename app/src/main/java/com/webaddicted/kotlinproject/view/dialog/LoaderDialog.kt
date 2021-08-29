package com.webaddicted.kotlinproject.view.dialog

import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.DialogLoaderBinding
import com.webaddicted.kotlinproject.global.common.DialogUtil
import com.webaddicted.kotlinproject.view.base.BaseDialog

class LoaderDialog : BaseDialog() {
    private lateinit var mBinding: DialogLoaderBinding

    companion object {
        val TAG = LoaderDialog::class.java.simpleName
        fun dialog(): LoaderDialog {
            val dialog= LoaderDialog()
            return dialog
        }
    }

    override fun getLayout(): Int {
        return R.layout.dialog_loader
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as DialogLoaderBinding
    }

    override fun onResume() {
        super.onResume()
        dialog?.let { DialogUtil.modifyDialogBounds(activity, it) }
    }

}

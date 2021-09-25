package com.webaddicted.kotlinproject.view.dialog

import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.DialogLoaderBinding
import com.webaddicted.kotlinproject.global.common.DialogUtil
import com.webaddicted.kotlinproject.view.base.BaseDialog

class LoaderDialog : BaseDialog(R.layout.dialog_loader) {
    private lateinit var mBinding: DialogLoaderBinding

    companion object {
        val TAG = LoaderDialog::class.qualifiedName
        fun dialog(): LoaderDialog {
            return LoaderDialog()
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as DialogLoaderBinding
    }

    override fun onResume() {
        super.onResume()
        dialog?.let { DialogUtil.modifyDialogBounds(activity, it) }
    }

}

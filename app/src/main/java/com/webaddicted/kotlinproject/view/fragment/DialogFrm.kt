package com.webaddicted.kotlinproject.view.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.DialogCustomBinding
import com.webaddicted.kotlinproject.databinding.FrmDialogBinding
import com.webaddicted.kotlinproject.global.common.DialogUtil
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.showToast
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.dialog.LoginDialog
import java.util.*

class DialogFrm : BaseFragment(R.layout.frm_dialog) {
    private lateinit var mBinding: FrmDialogBinding

    companion object {
        val TAG = DialogFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): DialogFrm {
            val fragment = DialogFrm()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmDialogBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.dialog_title)
    }

    private fun clickListener() {
        mBinding.btnSingleClick.setOnClickListener(this)
        mBinding.btnTwoEventDialog.setOnClickListener(this)
        mBinding.btnCustomDialog.setOnClickListener(this)
        mBinding.btnDialogFragment.setOnClickListener(this)
        mBinding.btnSelectionList.setOnClickListener(this)
        mBinding.btnListDialog.setOnClickListener(this)
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_single_click -> DialogUtil.showOkDialog(
                mActivity,
                getString(R.string.app_name),
                getString(R.string.dummyText),
                getString(R.string.ok)
            ) { dialog, which ->
                GlobalUtility.showToast("ok click $which")
                dialog.dismiss()
            }
            R.id.btn_two_event_dialog -> DialogUtil.showOkCancelDialog(
                mActivity,
                getString(R.string.app_name),
                getString(R.string.dummyText),
                { dialog, which ->
                    GlobalUtility.showToast("ok click")
                    dialog.dismiss()
                }
            ) { dialog, which ->
                GlobalUtility.showToast("cancel click")
                dialog.dismiss()
            }
            R.id.btn_custom_dialog -> customDialog()
            R.id.btn_dialog_fragment -> {
                val loginDialog = LoginDialog()
                parentFragmentManager.let { loginDialog.show(it, LoginDialog.TAG) }
            }
            R.id.btn_selection_list -> DialogUtil.getSingleChoiceDialog(mActivity,
                resources.getString(R.string.select_country),
                getCountryList(),
                { dialog, position ->
                    if (position > 0)
                        activity?.showToast(getCountryList()[position - 1])
                    dialog.dismiss()
                },
                { dialog, position -> dialog.dismiss() })
            R.id.btn_list_dialog -> DialogUtil.showListDialog(
                mActivity,
                resources.getString(R.string.select_country),
                getCountryList()
            ) { dialog, which ->
                activity?.showToast(getCountryList()[which])
                dialog.dismiss()
            }
            R.id.img_back -> activity?.onBackPressed()
        }
    }

    fun getCountryList(): List<String> {
        val list = ArrayList<String>()
        list.add("India")
        list.add("Japan")
        list.add("Armenia")
        list.add("Australia")
        list.add("Bangladesh")
        return list
    }

    private fun customDialog() {
        val dialog = Dialog(mActivity, R.style.AlertDialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.attributes?.windowAnimations = R.style.DialogSlideUpAnimation
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogBinding = GlobalUtility.getLayoutBinding(
            mActivity,
            R.layout.dialog_custom
        ) as DialogCustomBinding
        dialog.setContentView(dialogBinding.root)
        dialogBinding.txtTitle.text = getString(R.string.app_name)
        dialogBinding.txtMessage.text = resources.getString(R.string.dummyText)
        dialogBinding.btnOk.setOnClickListener { v ->
            GlobalUtility.showToast(resources.getString(R.string.ok))
            dialog.dismiss()
        }
        dialogBinding.txtCancel.setOnClickListener { v ->
            activity?.showToast(resources.getString(R.string.cancel))
            dialog.dismiss()
        }
        dialog.show()
    }
}


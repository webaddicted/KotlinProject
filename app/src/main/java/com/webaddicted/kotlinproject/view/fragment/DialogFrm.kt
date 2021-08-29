package com.webaddicted.kotlinproject.view.fragment

import android.app.Dialog
import android.content.DialogInterface
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

class DialogFrm : BaseFragment() {
    private lateinit var mBinding: FrmDialogBinding

    companion object {
        val TAG = DialogFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): DialogFrm {
            val fragment = DialogFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_dialog
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
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
                activity!!,
                getString(R.string.app_name),
                getString(R.string.dummyText),
                getString(R.string.ok),
                object :
                    DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        GlobalUtility.showToast("ok click $which")
                        dialog.dismiss()
                    }
                })
            R.id.btn_two_event_dialog -> DialogUtil.showOkCancelDialog(
                activity!!,
                getString(R.string.app_name),
                getString(R.string.dummyText),
                object :
                    DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        GlobalUtility.showToast("ok click")
                        dialog.dismiss()
                    }
                },
                object :
                    DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        GlobalUtility.showToast("cancel click")
                        dialog.dismiss()
                    }
                })
            R.id.btn_custom_dialog -> customDialog()
            R.id.btn_dialog_fragment -> {
                val loginDialog = LoginDialog()
                fragmentManager?.let { loginDialog.show(it, LoginDialog.TAG) }
            }
            R.id.btn_selection_list -> DialogUtil.getSingleChoiceDialog(activity!!,
                resources.getString(R.string.select_country),
                getCountryList(),
                DialogInterface.OnClickListener { dialog, position ->
                    if (position > 0)
                        activity?.showToast(getCountryList().get(position - 1).toString())
                    dialog.dismiss()
                },
                DialogInterface.OnClickListener { dialog, position -> dialog.dismiss() })
            R.id.btn_list_dialog -> DialogUtil.showListDialog(activity!!,
                resources.getString(R.string.select_country),
                getCountryList(),
                DialogInterface.OnClickListener { dialog, which ->
                    activity?.showToast(getCountryList().get(which).toString())
                    dialog.dismiss()
                })
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
        val dialog = Dialog(activity!!, R.style.AlertDialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.attributes.windowAnimations = R.style.DialogSlideUpAnimation
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogBinding = GlobalUtility.getLayoutBinding(
            activity!!,
            R.layout.dialog_custom
        ) as DialogCustomBinding
        dialog.setContentView(dialogBinding.root)
        dialogBinding.txtTitle.text = getString(R.string.app_name)
        dialogBinding.txtMessage.text = resources.getString(R.string.dummyText)
        dialogBinding.btnOk.setOnClickListener({ v ->
            GlobalUtility.showToast(resources.getString(R.string.ok))
            dialog.dismiss()
        })
        dialogBinding.txtCancel.setOnClickListener({ v ->
            activity?.showToast(resources.getString(R.string.cancel))
            dialog.dismiss()
        })
        dialog.show()
    }

}


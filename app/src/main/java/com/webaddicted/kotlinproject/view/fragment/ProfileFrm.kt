package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmProfileBinding
import com.webaddicted.kotlinproject.global.annotationdef.MediaPickerType
import com.webaddicted.kotlinproject.global.common.showImage
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.dialog.ImagePickerDialog
import com.webaddicted.kotlinproject.view.interfac.OnImageActionListener
import java.io.File

class ProfileFrm : BaseFragment() {
    private lateinit var imgPickerDialog: ImagePickerDialog
    private lateinit var mBinding: FrmProfileBinding

    companion object {
        val TAG = ProfileFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): ProfileFrm {
            val fragment = ProfileFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_profile
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmProfileBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.my_profile)
    }

    private fun clickListener() {
        mBinding.btnCaptureImage.setOnClickListener(this)
        mBinding.btnPickImage.setOnClickListener(this)
        mBinding.toolbar.imgBack.setOnClickListener(this)
//        mBinding.toolbar.imgBack.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_capture_image -> requestCamera(MediaPickerType.CAPTURE_IMAGE)
            R.id.btn_pick_image -> requestCamera(MediaPickerType.SELECT_IMAGE)
            R.id.img_back -> activity?.onBackPressed()
        }
    }

    private fun requestCamera(@MediaPickerType.MediaType captureImage: Int) {
        imgPickerDialog = ImagePickerDialog.dialog(captureImage,
            object : OnImageActionListener {
                override fun onAcceptClick(file: List<File>) {
                    mBinding.imgProfile.showImage(file.get(0), getPlaceHolder(0))
                }
            })
        fragmentManager?.let { imgPickerDialog.show(it, ImagePickerDialog.TAG) }
    }

}


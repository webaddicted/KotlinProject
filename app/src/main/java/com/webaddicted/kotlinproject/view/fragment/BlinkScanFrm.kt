package com.webaddicted.kotlinproject.view.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmBlinkScanBinding
import com.webaddicted.kotlinproject.global.blink.CameraActivity
import com.webaddicted.kotlinproject.global.common.CommonDataRespo.Companion.dlMutableImg
import com.webaddicted.kotlinproject.global.common.CommonDataRespo.Companion.selfieMutableImg
import com.webaddicted.kotlinproject.global.common.DialogUtil
import com.webaddicted.kotlinproject.global.common.FileHelper
import com.webaddicted.kotlinproject.global.common.showImage
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.io.File

class BlinkScanFrm : BaseFragment(R.layout.frm_blink_scan) {
    private var dlFrontImg: File?= null
    private var selfieImg: File? = null
    private var isSelfieClick: Boolean = false
    private lateinit var mBinding: FrmBlinkScanBinding

    companion object {
        val TAG = BlinkScanFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): BlinkScanFrm {
            val fragment = BlinkScanFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmBlinkScanBinding
        init()
        clickListener()
    }

    private fun init() {
        selfieMutableImg = MutableLiveData<Bitmap>()
        dlMutableImg = MutableLiveData<Bitmap>()

        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.blink_scan_title)
        selfieMutableImg.observe(this, { bitmap ->
            if (bitmap != null) {
                selfieImg = FileHelper.saveBitmapImage(bitmap)
                mBinding.imgCapturePic.showImage(selfieImg, getPlaceHolder(0))
            }
        })
        dlMutableImg.observe(this, { bitmap ->
            if (bitmap != null) {
                dlFrontImg = FileHelper.saveBitmapImage(bitmap)
                mBinding.imgScanDl.showImage(dlFrontImg, getPlaceHolder(0))
            }
        })
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.imgScanDl.setOnClickListener(this)
        mBinding.imgCapturePic.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.img_scan_dl -> {
                isSelfieClick = false
                checkBlinkPermission()
            }
            R.id.img_capture_pic -> {
                isSelfieClick = true
                checkBlinkPermission()
            }
        }
    }
    override fun onPermissionGranted(mCustomPermission: List<String>) {
        super.onPermissionGranted(mCustomPermission)
        val message: String = if (isSelfieClick) getString(R.string.plz_blink_for_selfie)
        else getString(R.string.plz_scan_dl)
        showMessage(message)
    }

    private fun showMessage(message: String) {
        DialogUtil.showOkDialog(
            mActivity,
            getString(R.string.app_name),
            message,
            getString(R.string.ok)
        ) { dialog, which ->
            dialog.dismiss()
            val intent = Intent(activity, CameraActivity::class.java)
            intent.putExtra("isOpenFronCamera", isSelfieClick)
            activity?.startActivity(intent)
        }
    }
}


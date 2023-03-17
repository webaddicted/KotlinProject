package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmZoomBinding
import com.webaddicted.kotlinproject.global.common.showImage
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.io.File


class ZoomImageFrm : BaseFragment(R.layout.frm_zoom) {
    private lateinit var mBinding: FrmZoomBinding

    companion object {
        val TAG = ZoomImageFrm::class.qualifiedName
        const val IMAGE_PATH = "image_path"
        const val IS_LOCAL_FILE = "isLocalFile"
        fun getInstance(url: String, isLocalFile: Boolean): ZoomImageFrm {
            val fragment = ZoomImageFrm()
            val bundle = Bundle()
            bundle.putString(IMAGE_PATH, url)
            bundle.putBoolean(IS_LOCAL_FILE, isLocalFile)
            fragment.arguments = bundle
            return fragment
        }

        fun getInstance(bundle: Bundle): ZoomImageFrm {
            val fragment = ZoomImageFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding? ) {
        mBinding = binding as FrmZoomBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.zoom_title)
        val image = arguments?.getString(IMAGE_PATH)
        val isLocalFile = arguments?.getBoolean(IS_LOCAL_FILE)
        if (image != null && image.isNotEmpty()) {
            if (isLocalFile!!) {
                mBinding.imgZoom.showImage(File(image), getPlaceHolder(2))
            } else {
                mBinding.imgZoom.showImage(image, getPlaceHolder(2))
            }
        } else mBinding.imgZoom.showImage("image", getPlaceHolder(4))
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> backDispatcher.onBackPressed()
        }
    }
}


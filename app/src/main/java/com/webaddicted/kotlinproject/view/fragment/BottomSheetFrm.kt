package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmBottomSheetBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment


class BottomSheetFrm : BaseFragment(R.layout.frm_bottom_sheet) {
    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var mBinding: FrmBottomSheetBinding

    companion object {
        val TAG = BottomSheetFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): BottomSheetFrm {
            val fragment = BottomSheetFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmBottomSheetBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.bottom_sheet)
        sheetBehavior = BottomSheetBehavior.from(mBinding.includeBottomSheet.bottomSheet)
        sheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(
                @NonNull bottomSheet: View,
                newState: Int
            ) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        mBinding.includeContent.btnBottomSheet.text = "Close Sheet"
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        mBinding.includeContent.btnBottomSheet.text = "Expand Sheet"
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(
                @NonNull bottomSheet: View,
                slideOffset: Float
            ) {
            }
        })
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.includeContent.btnBottomSheet.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_bottom_sheet -> {
                if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    mBinding.includeContent.btnBottomSheet.text = "Close sheet"
                } else {
                    sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    mBinding.includeContent.btnBottomSheet.text = "Expand sheet"
                }
            }
        }
    }
}


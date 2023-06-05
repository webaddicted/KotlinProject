package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmBottomSheetBinding
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.adapter.CommonAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment


class BottomSheetBehavFrm : BaseFragment(R.layout.frm_bottom_sheet) {
    private lateinit var adapter: CommonAdapter
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private lateinit var mBinding: FrmBottomSheetBinding

    companion object {
        val TAG = BottomSheetBehavFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): BottomSheetBehavFrm {
            val fragment = BottomSheetBehavFrm()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmBottomSheetBinding
        init()
        clickListener()
        setAdapter()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.bottom_sheet)
        bottomSheetBehavior =
            BottomSheetBehavior.from(mBinding.bottomSheetBehav.root)
        bottomSheetBehavior?.setBottomSheetCallback(
            object : BottomSheetCallback() {
                override fun onStateChanged(
                    bottomSheet: View,
                    newState: Int
                ) {
                }

                override fun onSlide(
                    bottomSheet: View,
                    slideOffset: Float
                ) {
                    (bottomSheetBehavior?.peekHeight!!).coerceAtMost(bottomSheet.height)
                }
            })
        bottomSheetBehavior!!.peekHeight = 550//preview.getHeight() / 2
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.includeContent.btnBottomSheetDialog.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> requireActivity().onBackPressedDispatcher.onBackPressed()
            R.id.btn_bottom_sheet_dialog -> {
                bottomSheetBehavior!!.peekHeight = 550//preview.getHeight() / 2
                bottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }
    }

    private fun setAdapter() {
//        mBinding.bottomSheetBehav.imgNoDataFound.gone()
//        adapter = CommonAdapter()
//        mBinding.bottomSheetBehav.rvApps.layoutManager = LinearLayoutManager(
//            activity,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//        mBinding.bottomSheetBehav.rvApps.adapter = adapter
    }
}


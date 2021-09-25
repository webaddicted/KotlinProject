package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmCollapseExpBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment


class CollapseExpendFrm : BaseFragment(R.layout.frm_collapse_exp) {
    private lateinit var mBinding: FrmCollapseExpBinding

    companion object {
        val TAG = CollapseExpendFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): CollapseExpendFrm {
            val fragment = CollapseExpendFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmCollapseExpBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.collapse)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnCollExp.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_coll_exp -> {
                if (mBinding.expandableLayout.isExpanded) {
                    mBinding.expandableLayout.collapse()
                    mBinding.btnCollExp.text = "Expend"
                } else {
                    mBinding.expandableLayout.expand()
                    mBinding.btnCollExp.text = "Collapse"
                }
            }
        }
    }

}


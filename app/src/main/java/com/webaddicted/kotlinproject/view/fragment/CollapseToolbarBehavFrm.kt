package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmCollapseToolbarBehavBinding
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.view.adapter.CommonAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import kotlin.math.abs


class CollapseToolbarBehavFrm : BaseFragment() {
    private lateinit var adapter: CommonAdapter
    private lateinit var mBinding: FrmCollapseToolbarBehavBinding

    companion object {
        val TAG = CollapseToolbarBehavFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): CollapseToolbarBehavFrm {
            val fragment = CollapseToolbarBehavFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_collapse_toolbar_behav
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmCollapseToolbarBehavBinding
        init()
        clickListener()
    }

    private fun init() {
//        mBinding.imgBack.visible()
//        mBinding.txtToolbarTitle.text = getString(R.string.collapse_toolbar_title)
//        mBinding.imgProfile.visible()
//        mBinding.imgSort.visible()
        setCollapse()
        setAdapter()
    }

    private fun clickListener() {
        mBinding.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
        }
    }

    private fun setAdapter() {
        adapter = CommonAdapter()
        mBinding.recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mBinding.recyclerView.adapter = adapter
    }

    private fun setCollapse() {
        mBinding.rippleBackground.startRippleAnimation()
        mBinding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            mBinding.imgBack.alpha = abs(
                verticalOffset / appBarLayout.totalScrollRange.toFloat()
            ) * 2 - 1.0f
            if (abs(verticalOffset / mBinding.appbar.totalScrollRange.toFloat()) >= 0.35) {
                mBinding.rippleBackground.stopRippleAnimation()
            } else {
                mBinding.rippleBackground.startRippleAnimation()
            }
            if (abs(verticalOffset / mBinding.appbar.totalScrollRange.toFloat()) >= 1) {
                Lg.d(
                    TAG,
                    "scroll if : " + abs(verticalOffset / mBinding.appbar.totalScrollRange.toFloat())
                )
            } else {
                Lg.d(
                    TAG,
                    "scroll: else : " + abs(verticalOffset / mBinding.appbar.totalScrollRange.toFloat())
                )
            }
        })
    }

}


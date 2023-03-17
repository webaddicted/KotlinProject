package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmCollapseToolbarBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.adapter.CommonAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import kotlin.math.abs


class CollapseToolbarFrm : BaseFragment(R.layout.frm_collapse_toolbar) {
    private lateinit var adapter: CommonAdapter
    private lateinit var mBinding: FrmCollapseToolbarBinding

    companion object {
        val TAG = CollapseToolbarFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): CollapseToolbarFrm {
            val fragment = CollapseToolbarFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmCollapseToolbarBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.imgBack.visible()
        mBinding.txtToolbarTitle.text = getString(R.string.collapse_toolbar_title)
        mBinding.imgProfile.visible()
        mBinding.imgSort.visible()
        setCollapse()
        setAdapter()
    }

    private fun clickListener() {
        mBinding.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> backDispatcher.onBackPressed()
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
        mBinding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            mBinding.imgSort.alpha = 1.0f - abs(
                verticalOffset / appBarLayout.totalScrollRange.toFloat()
            )
            mBinding.txtToolbarTitle.alpha = abs(
                verticalOffset / appBarLayout.totalScrollRange.toFloat()
            )
            mBinding.imgProfile.alpha = 1.0f - abs(
                verticalOffset / appBarLayout.totalScrollRange.toFloat()
            ) * 2
            mBinding.imgBack.alpha = abs(
                verticalOffset / appBarLayout.totalScrollRange.toFloat()
            ) * 2 - 1.0f
        })
    }

}


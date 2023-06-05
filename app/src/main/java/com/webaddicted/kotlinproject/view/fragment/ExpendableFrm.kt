package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmExpendViewBinding
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.adapter.ExpendableListAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.util.*

class ExpendableFrm : BaseFragment(R.layout.frm_expend_view) {
    private var mSpinnerAdapter: SpinnerListAdapter? = null
    private var mExpendAdapter: ExpendableListAdapter? = null
    private lateinit var mBinding: FrmExpendViewBinding

    companion object {
        val TAG = ExpendableFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): ExpendableFrm {
            val fragment = ExpendableFrm()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmExpendViewBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.expendable_title)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnExpendable.setOnClickListener(this)
        mBinding.btnSpinner.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> requireActivity().onBackPressedDispatcher.onBackPressed()
            R.id.btn_expendable -> setExpendAdapter()
            R.id.btn_spinner -> setSpinnerAdapter()
        }
    }

    private fun setExpendAdapter() {
        mBinding.lvExpandable.visible()
        mBinding.spinner.gone()
        mExpendAdapter = ExpendableListAdapter(activity)
        mBinding.lvExpandable.setAdapter(mExpendAdapter)
    }

    private fun setSpinnerAdapter() {
        mBinding.lvExpandable.gone()
        mBinding.spinner.visible()
        mSpinnerAdapter = SpinnerListAdapter(getListBean())
        mBinding.spinner.adapter = mSpinnerAdapter
    }

    private fun getListBean(): List<String> {
        val mList = ArrayList<String>()
        for (i in 0..49) {
            if (i == 0) mList.add("Click me")
            else mList.add("task - # $i")
        }
        return mList
    }
}


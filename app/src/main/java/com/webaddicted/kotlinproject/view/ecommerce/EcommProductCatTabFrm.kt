package com.webaddicted.kotlinproject.view.ecommerce

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmEcomProductCatTabBinding
import com.webaddicted.kotlinproject.view.adapter.EcomProductCatAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment

class EcommProductCatTabFrm : BaseFragment() {
    //    private lateinit var mListAdapter: EcomProductCatAdapter
    private lateinit var mBinding: FrmEcomProductCatTabBinding

    companion object {
        val TAG = EcommProductCatTabFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): EcommProductCatTabFrm {
            val fragment = EcommProductCatTabFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_ecom_product_cat_tab
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmEcomProductCatTabBinding
        init()
    }

    private fun init() {
        setListAdapter()
    }

    private fun setListAdapter() {
        val mListAdapter = EcomProductCatAdapter()
        mBinding.rvProductCat.layoutManager = GridLayoutManager(activity, 2)
        val animation = AnimationUtils.loadLayoutAnimation(context, R.anim.rv_anim_down_to_up)
        mBinding.rvProductCat.layoutAnimation = animation
        mBinding.rvProductCat.adapter = mListAdapter
    }

//    override fun onResume() {
//        super.onResume()
//        addBlankSpace(mBinding.bottomSpace)
//    }
    /**
     * navigate on fragment
     *
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            EcommOtpFrm.TAG -> frm = EcommOtpFrm.getInstance(Bundle())
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }

}


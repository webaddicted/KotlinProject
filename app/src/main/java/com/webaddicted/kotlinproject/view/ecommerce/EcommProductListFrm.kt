package com.webaddicted.kotlinproject.view.ecommerce

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmEcomProductBinding
import com.webaddicted.kotlinproject.global.misc.ViewPagerAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment

class EcommProductListFrm : BaseFragment() {
    private lateinit var mBinding: FrmEcomProductBinding

    companion object {
        val TAG = EcommProductListFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): EcommProductListFrm {
            val fragment = EcommProductListFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_ecom_product
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmEcomProductBinding
        setupViewPager(mBinding.pager)
        clickListener()
    }

    private fun clickListener() {
        mBinding.imgBack.setOnClickListener(this)
        mBinding.tabLayout.setupWithViewPager(mBinding.pager)
        mBinding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mBinding.pager.setCurrentItem(position, false)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(fragmentManager!!)
        adapter.addFragment(EcommProductCatTabFrm(), "Popular")
        adapter.addFragment(EcommProductCatTabFrm(), "Low Price")
        adapter.addFragment(EcommProductCatTabFrm(), "Hight Price")
        adapter.addFragment(EcommProductCatTabFrm(), "Assured")
        viewPager.adapter = adapter
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


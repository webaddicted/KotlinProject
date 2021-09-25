package com.webaddicted.kotlinproject.view.fragment

import ViewPager2Adapter
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmViewpagerTabBinding
import com.webaddicted.kotlinproject.databinding.RowTabViewBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.global.misc.ViewPagerAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment


class ViewPagerTabFrm : BaseFragment() {
    private lateinit var mBinding: FrmViewpagerTabBinding
    private val tabTitle = arrayOf("MOVIE", "FOOD", "TRAVEL")
    private val unreadCount = intArrayOf(0, 5, 0)

    companion object {
        val TAG = ViewPagerTabFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): ViewPagerTabFrm {
            val fragment = ViewPagerTabFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_viewpager_tab
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmViewpagerTabBinding
        init()
        clickListener()
        pagerSetup()
    }


    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.view_pager)
        dynamicSetupTabIcons()
        addTabs(mBinding.tabWithoutIcon, true)
        addTabs(mBinding.tabCustomIcon, true)

    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
        }
    }

    private fun pagerSetup() {
        mBinding.viewPager.offscreenPageLimit = 3
        mBinding.tabCustomIcon.setupWithViewPager(mBinding.viewPager)
        mBinding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mBinding.viewPager.setCurrentItem(position, false)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        setupViewPager(mBinding.viewPager)

    }

    private fun addTabs(tabLayout: TabLayout, hasIcon: Boolean) {
        for (i in tabTitle.indices) {
            val tabitem = tabLayout.newTab()
            if (hasIcon) tabitem.customView = prepareTabView(i)
            tabLayout.addTab(tabitem)
            //            mBinding.tabCustomIcon.getTabAt(i).setCustomView(prepareTabView(i));
        }
    }

    private fun prepareTabView(pos: Int): View {
        val customBinding: RowTabViewBinding = GlobalUtility.getLayoutBinding(
            activity,
            R.layout.row_tab_view
        ) as RowTabViewBinding
        customBinding.tvTitle.text = tabTitle[pos]
        if (unreadCount[pos] > 0) {
            customBinding.tvCount.visible()
            customBinding.tvCount.text = "${unreadCount[pos]}"
        } else customBinding.tvCount.gone()
        return customBinding.root
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(fragmentManager!!)
        val frmCalender = CalendarFrm()
        val frmAnim = AnimationFrm()
        val frmBlink = BlinkScanFrm()
        adapter.addFragment(frmCalender, "Calender")
        adapter.addFragment(frmAnim, "Animation")
        adapter.addFragment(frmBlink, "Blink Scan")
        viewPager.adapter = adapter
    }


    private fun dynamicSetupTabIcons() {
        mBinding.viewPager.offscreenPageLimit = tabTitle.size
        dynamicSetupViewPager2(mBinding.viewPager2)
        TabLayoutMediator(
            mBinding.tabDynamic2, mBinding.viewPager2
        ) { tab: TabLayout.Tab, position: Int ->
            tab.customView = prepareTabView(position)
        }.attach()
    }

    private fun dynamicSetupViewPager2(viewPager: ViewPager2) {
        val adapter = ViewPager2Adapter(requireActivity())
        val frmCalender = CalendarFrm()
        val frmAnim = AnimationFrm()
        val frmBlink = BlinkScanFrm()
        adapter.addFragment(frmCalender, "Calender")
        adapter.addFragment(frmAnim, "Animation")
        adapter.addFragment(frmBlink, "Blink Scan")
        viewPager.adapter = adapter
    }
}


package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.NonNull
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.roughike.bottombar.BottomBarTab
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmBottomNaviBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment


class BottomNavigationFrm : BaseFragment(R.layout.frm_bottom_navi) {
    private lateinit var mBinding: FrmBottomNaviBinding

    companion object {
        val TAG = BottomNavigationFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): BottomNavigationFrm {
            val fragment = BottomNavigationFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmBottomNaviBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.bottom_navigation)
        bottomNavi()
    }

    private fun bottomNavi() {
        val nearby: BottomBarTab = mBinding.navBadge.getTabWithId(R.id.tab_profile)
        nearby.setBadgeCount(2)
        mBinding.navMeow.add(MeowBottomNavigation.Model(1, R.drawable.ic_food))
        mBinding.navMeow.add(MeowBottomNavigation.Model(2, R.drawable.ic_movie))
        mBinding.navMeow.add(MeowBottomNavigation.Model(3, R.drawable.ic_travel))
        mBinding.navMeow.add(MeowBottomNavigation.Model(4, R.drawable.ic_profile))
        mBinding.navMeow.add(MeowBottomNavigation.Model(5, R.drawable.ic_no_simcard))

        mBinding.navNormal.setOnTabSelectListener {
            getSelectedTab(it, false)
        }
        mBinding.navCustom.setOnTabSelectListener {
            getSelectedTab(it, false)
        }
        mBinding.navColor.setOnTabSelectListener {
            getSelectedTab(it, false)
        }
        mBinding.navBadge.setOnTabSelectListener {
            getSelectedTab(it, false)
        }
        mBinding.navMeow.setOnShowListener {
            // YOUR CODES
        }

        mBinding.navMeow.setOnClickMenuListener {
            // YOUR CODES
        }
        mBinding.navDefault.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        openFragment(CalendarFrm.getInstance(Bundle()))
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

    private fun getSelectedTab(menuItemId: Int, isReselection: Boolean): String {
        var message = "Content for "
        when (menuItemId) {
            R.id.tab_profile -> message += "recents"
            R.id.tab_food -> message += "favorites"
            R.id.tab_movie -> message += "nearby"
            R.id.tab_sim_card -> message += "friends"
            R.id.tab_travel -> message += "food"
        }
        if (isReselection) {
            message += " WAS RESELECTED! YAY!"
        }
        return message
    }

    private val navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.nav_food -> {
                        openFragment(CalendarFrm.getInstance(Bundle()))
                        return true
                    }
                    R.id.nav_movie -> {
                        openFragment(DialogFrm.getInstance(Bundle()))
                        return true
                    }
                    R.id.nav_travel -> {
                        openFragment(ProfileFrm.getInstance(Bundle()))
                        return true
                    }
                    R.id.nav_profile -> {
                        openFragment(SharedPrefFrm.getInstance(Bundle()))
                        return true
                    }
                    R.id.nav_no_sim -> {
                        openFragment(ShareDataFrm.getInstance(Bundle()))
                        return true
                    }
                }
                return false
            }
        }

    fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.nav_container, fragment)
//        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}


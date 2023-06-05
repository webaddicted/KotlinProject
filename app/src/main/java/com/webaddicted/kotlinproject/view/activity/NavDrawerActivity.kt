package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityNavDrawerBinding
import com.webaddicted.kotlinproject.global.common.AppApplication.Companion.context
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.view.base.BaseActivity
import com.webaddicted.kotlinproject.view.fragment.NewsFrm

//import kotlinx.android.synthetic.main.nav_header_main.view.*


/**
 * Created by Deepak Sharma on 01/07/19.
 */
class NavDrawerActivity : BaseActivity(R.layout.activity_nav_drawer) {
    private lateinit var mBinding: ActivityNavDrawerBinding

    companion object {
        val TAG: String = NavDrawerActivity::class.java.simpleName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, NavDrawerActivity::class.java))
        }
    }

    override fun onBindTo(binding: ViewDataBinding) {
        mBinding = binding as ActivityNavDrawerBinding
        init()
        clickListener()
    }

    private fun init() {
        backDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START))
                    mBinding.drawerLayout.closeDrawer(GravityCompat.START)
                else backDispatcher.onBackPressed()
            }
        })
        setNavigationColor(ContextCompat.getColor(context, R.color.app_color))
        mBinding.toolbar.imgNavRight.gone()
        mBinding.txtSuggest.text = "Swipe left for left navigation"
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.navigation_drawer)
        navigationDrawer()
    }

    private fun navigationDrawer() {
        val navView = mBinding.navView.getHeaderView(0)
//        navView.txt_create_lead.setOnClickListener(this)
//        navView.txt_logout.setOnClickListener(this)
//        navView.txt_home.setOnClickListener(this)
//        navView.txt_profile.setOnClickListener(this)
//        navView.txt_faq.setOnClickListener(this)
    }

    private fun clickListener() {
        mBinding.toolbar.imgNavLeft.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_nav_left -> openCloseDrawer(true)
            R.id.txt_create_lead, R.id.txt_logout, R.id.txt_home,
            R.id.txt_profile, R.id.txt_faq -> onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun openCloseDrawer(openDrawer: Boolean) {
        if (openDrawer) mBinding.drawerLayout.openDrawer(GravityCompat.START)
        else mBinding.drawerLayout.closeDrawer(GravityCompat.START)
    }
    /**
     * navigate on fragment
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            NewsFrm.TAG -> frm = NewsFrm.getInstance(Bundle())
        }
        if (frm != null) {
            navigateFragment(R.id.container, frm, false)
        }
    }

}
package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityNavBothDrawerBinding
import com.webaddicted.kotlinproject.global.common.AppApplication.Companion.context
import com.webaddicted.kotlinproject.view.base.BaseActivity
//import kotlinx.android.synthetic.main.nav_header_main.view.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class NavBothSideDrawerActivity : BaseActivity() {
    private lateinit var mBinding: ActivityNavBothDrawerBinding

    companion object {
        val TAG: String = NavBothSideDrawerActivity::class.java.simpleName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, NavBothSideDrawerActivity::class.java))
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_nav_both_drawer
    }

    override fun initUI(binding: ViewDataBinding) {
        mBinding = binding as ActivityNavBothDrawerBinding
        init()
        clickListener()
    }

    private fun init() {
        setNavigationColor(ContextCompat.getColor(context, R.color.app_color))
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.navigation_drawer)
        mBinding.txtSuggest.text =
            "Swipe left for left navigation \nor\n Swipe right for right navigation"
    }

    private fun clickListener() {
        mBinding.toolbar.imgNavLeft.setOnClickListener(this)
        mBinding.toolbar.imgNavRight.setOnClickListener(this)
        val navLeftView = mBinding.navLeftView.getHeaderView(0)
        val navRightView = mBinding.navRightView.getHeaderView(0)
        setNavClick(navLeftView)
        setNavClick(navRightView)
    }

    private fun setNavClick(navView: View) {
//        navView.txt_create_lead.setOnClickListener(this)
//        navView.txt_logout.setOnClickListener(this)
//        navView.txt_home.setOnClickListener(this)
//        navView.txt_profile.setOnClickListener(this)
//        navView.txt_faq.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_nav_left -> openCloseDrawer(GravityCompat.START)
            R.id.img_nav_right -> openCloseDrawer(GravityCompat.END)
            R.id.txt_create_lead,R.id.txt_logout,R.id.txt_home,
            R.id.txt_profile,R.id.txt_faq->onBackPressed()
        }
    }

    private fun openCloseDrawer(openDrawer: Int) {
        if (!mBinding.drawerLayout.isDrawerOpen(openDrawer))
            mBinding.drawerLayout.openDrawer(openDrawer)
        else mBinding.drawerLayout.closeDrawer(openDrawer)
    }

    override fun onBackPressed() {
        when {
            mBinding.drawerLayout.isDrawerOpen(GravityCompat.START) ->
                mBinding.drawerLayout.closeDrawer(GravityCompat.START)
            mBinding.drawerLayout.isDrawerOpen(GravityCompat.END) ->
                mBinding.drawerLayout.closeDrawer(GravityCompat.END)
            else -> {
                super.onBackPressed()
            }
        }
    }

}
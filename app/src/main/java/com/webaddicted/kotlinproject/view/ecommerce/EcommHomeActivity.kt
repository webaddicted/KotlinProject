package com.webaddicted.kotlinproject.view.ecommerce

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityEcomHomeBinding
import com.webaddicted.kotlinproject.view.base.BaseActivity
//import kotlinx.android.synthetic.main.nav_header_main.view.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class EcommHomeActivity : BaseActivity() {
    private var imgView: ImageView? = null
    private lateinit var mBinding: ActivityEcomHomeBinding

    companion object {
        val TAG: String = EcommHomeActivity::class.java.simpleName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, EcommHomeActivity::class.java))
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_ecom_home
    }

    override fun initUI(binding: ViewDataBinding) {
        mBinding = binding as ActivityEcomHomeBinding
        navigateScreen(EcommHomeFrm.TAG)
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

    fun openCloseDrawer(openDrawer: Boolean) {
        if (openDrawer) mBinding.drawerLayout.openDrawer(GravityCompat.START)
        else mBinding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.txt_create_lead,R.id.txt_logout,R.id.txt_home,
            R.id.txt_profile,R.id.txt_faq->onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START))
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    /**
     * navigate on fragment
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            EcommHomeFrm.TAG -> frm = EcommHomeFrm.getInstance(Bundle())
        }
        if (frm != null) navigateFragment(R.id.container, frm, false)
    }

    fun setNavi(imgNavi: ImageView) {
        imgView = imgNavi
    }
}
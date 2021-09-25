package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.ViewPager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityOnboardingBinding
import com.webaddicted.kotlinproject.view.adapter.OnBordingViewPagerAdapter
import com.webaddicted.kotlinproject.view.base.BaseActivity

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class OnBoardActivity : BaseActivity(R.layout.activity_onboarding) {
    private lateinit var mBinding: ActivityOnboardingBinding
    private val layouts =
        intArrayOf(R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3, R.layout.welcome_slide4)
    companion object{
        val TAG: String = OnBoardActivity::class.java.simpleName
        fun newIntent(activity: Activity){
            activity.startActivity(Intent(activity, OnBoardActivity::class.java))
        }
    }

    override fun onBindTo(binding: ViewDataBinding) {
        mBinding = binding as ActivityOnboardingBinding
        init()
        clickListener()
        setAdapter()
    }

    private fun init() {
        mBinding.viewPager.addOnPageChangeListener(getChangeListener())
        changeNavigatColor(0)

    }

    private fun clickListener() {
        mBinding.btnGotIt.setOnClickListener(this)
        mBinding.btnNext.setOnClickListener(this)
        mBinding.btnSkip.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_skip, R.id.btn_got_it -> {
                LoginActivity.newIntent(this)
                finish()
            }
            R.id.btn_next -> showNextSlide()
        }
    }

    private fun setAdapter() {
        mBinding.viewPager.adapter = OnBordingViewPagerAdapter(this, layouts)
        mBinding.dotsIndicator.setViewPager(mBinding.viewPager)
    }

    private fun getChangeListener(): ViewPager.OnPageChangeListener {
        return object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                val isLastPage = position == layouts.size - 1
                mBinding.btnNext.visibility = if (isLastPage) View.GONE else View.VISIBLE
                mBinding.btnSkip.visibility = if (isLastPage) View.INVISIBLE else View.VISIBLE
                mBinding.btnGotIt.visibility = if (isLastPage) View.VISIBLE else View.GONE
                changeNavigatColor(position)
            }

            override fun onPageScrolled(arg: Int, arg1: Float, arg2: Int) {}

            override fun onPageScrollStateChanged(arg: Int) {}
        }
    }

    private fun changeNavigatColor(position: Int) {
        when (position) {
            0 -> setNavigationColor(ContextCompat.getColor(this,R.color.bg_screen1))
            1 -> setNavigationColor(ContextCompat.getColor(this,R.color.bg_screen2))
            2 -> setNavigationColor(ContextCompat.getColor(this,R.color.bg_screen3))
            3 -> setNavigationColor(ContextCompat.getColor(this,R.color.bg_screen4))
        }

    }

    private fun showNextSlide() {
        val nextIndex = mBinding.viewPager.currentItem + 1
        if (nextIndex < layouts.size)
            mBinding.viewPager.currentItem = nextIndex
    }
}
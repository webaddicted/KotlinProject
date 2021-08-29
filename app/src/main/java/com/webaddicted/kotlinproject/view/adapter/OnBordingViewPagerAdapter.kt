package com.webaddicted.kotlinproject.view.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class OnBordingViewPagerAdapter(activity: Activity, private val layouts: IntArray?) : PagerAdapter() {
    private val layoutInflater: LayoutInflater

    override fun getCount(): Int {
        if (layouts == null) return 0
        return layouts.size
    }

    init {
        layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = layoutInflater.inflate(layouts!![position], container, false)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}
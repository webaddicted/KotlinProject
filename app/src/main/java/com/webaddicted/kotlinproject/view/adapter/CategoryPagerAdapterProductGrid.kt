package com.webaddicted.kotlinproject.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.webaddicted.kotlinproject.view.ecommerce.EcommProductCatTabFrm

/**
 * Created by Deepak Sharma(webaddicted) on 19-11-2019.
 */
class CategoryPagerAdapterProductGrid( fm: FragmentManager, private var NumberOfTabs: Int) :
    FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return NumberOfTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> return EcommProductCatTabFrm()
            1 -> return EcommProductCatTabFrm()
            2 -> return EcommProductCatTabFrm()
            3 -> return EcommProductCatTabFrm()
            else -> EcommProductCatTabFrm()
        }
    }
}

package com.webaddicted.kotlinproject.view.adapter

import android.graphics.Paint
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowProductCatBinding
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseAdapter

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class EcomProductCatAdapter : BaseAdapter() {
    override fun getListSize(): Int {
//        if (list == null) return 0
        return 25//list?.size!!

    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_product_cat
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        if (rowBinding is RowProductCatBinding) {
            rowBinding.imgProduct.setImageResource(R.drawable.iphnx)
            rowBinding.offer.text = "\u20B9 63,999"
            rowBinding.offer.paintFlags = rowBinding.offer.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            if ((position == 0) or (position == 1)) rowBinding.text.visible()
            else rowBinding.text.gone()
        }
    }

//    fun notifyAdapter(prodList: ArrayList<EcommCateBean>) {
//        list = prodList
//        notifyDataSetChanged()
//    }
}
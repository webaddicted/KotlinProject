package com.webaddicted.kotlinproject.view.adapter

import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowSimItemBinding
import com.webaddicted.kotlinproject.model.bean.deviceinfo.CPUBean
import com.webaddicted.kotlinproject.view.base.BaseAdapter

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class CPUAdapter(private var list: java.util.ArrayList<CPUBean>?) : BaseAdapter() {
    override fun getListSize(): Int {
        if (list == null) return 0
        return list?.size!!
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_sim_item
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        if (rowBinding is RowSimItemBinding) {
            val source = list?.get(position)
            rowBinding.tvLabel.text = source?.featureLable
            rowBinding.tvSimInformation.text = source?.featureValue
        }
    }

    fun notifyAdapter(prodList: ArrayList<CPUBean>) {
        list = prodList
        notifyDataSetChanged()
    }
}
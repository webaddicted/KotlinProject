package com.webaddicted.kotlinproject.view.adapter

import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowTextListBinding
import com.webaddicted.kotlinproject.view.base.BaseAdapter

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class CommonAdapter : BaseAdapter() {

    override fun getListSize(): Int {
//        if (mTaskList == null) return 0
//        return mTaskList?.size!!
        return 150
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_text_list
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        if (rowBinding is RowTextListBinding) {
        }
    }

//    fun notifyAdapter(list: ArrayList<String>) {
//        this.mTaskList = list
//        notifyDataSetChanged()
//    }
}

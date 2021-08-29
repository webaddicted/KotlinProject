package com.webaddicted.kotlinproject.view.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowGridBinding
import com.webaddicted.kotlinproject.databinding.RowRecyclerListBinding
import com.webaddicted.kotlinproject.databinding.RowTextListBinding
import com.webaddicted.kotlinproject.global.common.showImage
import com.webaddicted.kotlinproject.view.base.BaseAdapter
import com.webaddicted.kotlinproject.view.fragment.RecyclerViewFrm

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class RecyclerListAdapter(
    private val recyclerViewFrm: RecyclerViewFrm,
    private var dataList: List<String>?,
    private val layoutId: Int
) : BaseAdapter() {

    override fun getListSize(): Int? {
        if (dataList == null) return 0
        return dataList?.size
    }

    override fun getLayoutId(viewType: Int): Int {
        return if (viewType == itemCount - 1) layoutId
        else R.layout.row_text_list
    }

    private object VIEW_TYPES {
        val NORMAL = 1
        val FOOTER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) VIEW_TYPES.FOOTER
        else VIEW_TYPES.NORMAL
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        when (rowBinding) {
            is RowRecyclerListBinding -> {
                val source = dataList?.get(position)
                rowBinding.imgInitial.showImage(
                    source,
                    getPlaceHolder(0)
                )
                onClickListener(rowBinding,rowBinding.imgInitial, position)
            }
            is RowGridBinding -> {
                val source = dataList?.get(position)
                rowBinding.img.showImage(
                    source,
                    getPlaceHolder(0)
                )
                onClickListener(rowBinding,rowBinding.img, position)
            }
            is RowTextListBinding -> {
                val mRowBinding = rowBinding
            }
        }
    }

    override fun getClickEvent(mRowBinding: ViewDataBinding,view: View?, position: Int) {
        super.getClickEvent(mRowBinding,view, position)
        when (view?.id) {
            R.id.img_initial -> {
            }
        }
    }

    fun notifyAdapter(list: ArrayList<String>?) {
        dataList = list
        notifyDataSetChanged()
    }
}
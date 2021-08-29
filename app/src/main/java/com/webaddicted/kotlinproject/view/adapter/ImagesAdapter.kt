package com.webaddicted.kotlinproject.view.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowGridBinding
import com.webaddicted.kotlinproject.global.common.showImage
import com.webaddicted.kotlinproject.model.bean.common.ImagesBean
import com.webaddicted.kotlinproject.view.base.BaseAdapter
import com.webaddicted.kotlinproject.view.fragment.PhoneImageFrm
import com.webaddicted.kotlinproject.view.fragment.ZoomImageFrm
import java.io.File

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class ImagesAdapter(
    private var frm: PhoneImageFrm,
    private var list: java.util.ArrayList<ImagesBean>?
) : BaseAdapter() {
    override fun getListSize(): Int {
        if (list == null) return 0
        return list?.size!!
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_grid
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        if (rowBinding is RowGridBinding) {
            val source = list?.get(position)
            rowBinding.img.showImage(File(source?.image), getPlaceHolder(2))
            onClickListener(rowBinding, rowBinding.img, position)
        }
    }

    override fun getClickEvent(mRowBinding: ViewDataBinding, view: View?, position: Int) {
        super.getClickEvent(mRowBinding, view, position)
        when (view?.id) {
            R.id.img -> {
                frm.navigateScreen(ZoomImageFrm.TAG, list?.get(position)?.image!!)
            }
        }
    }

    fun notifyAdapter(prodList: ArrayList<ImagesBean>) {
        list = prodList
        notifyDataSetChanged()
    }
}
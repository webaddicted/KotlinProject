package com.webaddicted.kotlinproject.view.adapter

import androidx.databinding.ViewDataBinding
import com.bumptech.glide.RequestManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowExoPlayerBinding
import com.webaddicted.kotlinproject.databinding.RowGridBinding
import com.webaddicted.kotlinproject.global.common.showImage
import com.webaddicted.kotlinproject.model.bean.common.ExoPlayerBean
import com.webaddicted.kotlinproject.view.base.BaseAdapter
import java.io.File

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class ExoPlayerAdapter(private var list: java.util.ArrayList<ExoPlayerBean>) : BaseAdapter() {
    override fun getListSize(): Int {
        return list.size
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_exo_player
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        if (rowBinding is RowExoPlayerBinding) {
            val source = list[position]
            var adasdas = rowBinding.root
            rowBinding.root.tag = rowBinding
            rowBinding.txtTitle.text = source.title
            rowBinding.txtUserHandler.text = source.userHandle
            rowBinding.imgMediaCover.showImage(source.mediaCoverImgUrl, getPlaceHolder(2))
        }
    }

    fun notifyAdapter(prodList: ArrayList<ExoPlayerBean>) {
        list = prodList
        notifyDataSetChanged()
    }
}
package com.webaddicted.kotlinproject.view.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowEcomCateBinding
import com.webaddicted.kotlinproject.global.common.showImage
import com.webaddicted.kotlinproject.model.bean.ecommerce.EcommCateBean
import com.webaddicted.kotlinproject.view.base.BaseAdapter
import com.webaddicted.kotlinproject.view.ecommerce.EcommHomeFrm
import com.webaddicted.kotlinproject.view.ecommerce.EcommProductListFrm

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class EcomFashionAdapter(private var frm: EcommHomeFrm, private var list: java.util.ArrayList<EcommCateBean>?) : BaseAdapter() {
    override fun getListSize(): Int {
        if (list == null) return 0
        return list?.size!!

    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_ecom_cate
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        if (rowBinding is RowEcomCateBinding) {
            val source = list?.get(position)
            rowBinding.imgProduct.showImage(source?.catImg, getPlaceHolder(0))
            rowBinding.txtProduct.text = source?.catName
            onClickListener(rowBinding,rowBinding.imgProduct, position)
        }
    }

    override fun getClickEvent(mRowBinding: ViewDataBinding,view: View?, position: Int) {
        super.getClickEvent(mRowBinding,view, position)
        when(view?.id){
            R.id.img_product-> frm.navigateScreen(EcommProductListFrm.TAG)
        }
    }
    fun notifyAdapter(prodList: ArrayList<EcommCateBean>) {
        list = prodList
        notifyDataSetChanged()
    }
}
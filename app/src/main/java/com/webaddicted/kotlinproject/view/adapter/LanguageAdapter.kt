package com.webaddicted.kotlinproject.view.adapter

import android.app.Activity
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowLanguageBinding
import com.webaddicted.kotlinproject.global.common.showImage
import com.webaddicted.kotlinproject.model.bean.language.LanguageBean
import com.webaddicted.kotlinproject.view.activity.LanguageActivity
import com.webaddicted.kotlinproject.view.base.BaseAdapter

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class LanguageAdapter(
    private val activity: Activity,
    private val languageList: List<LanguageBean>
) : BaseAdapter() {
    var selectedPos = -1
    override fun getListSize(): Int {
        return languageList.size
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_language
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        if (rowBinding is RowLanguageBinding) {
            val source = languageList[position]
            rowBinding.txtLanguageName.text = source.languageName
            rowBinding.imgCountryFlag.showImage(
                source.languageFlag,
                getPlaceHolder(1)
            )
            rowBinding.rbLanguage.isChecked = selectedPos == position
            onClickListener(rowBinding, rowBinding.rbLanguage, position)
        }
    }

    override fun getClickEvent(mRowBinding: ViewDataBinding, view: View?, position: Int) {
        super.getClickEvent(mRowBinding, view, position)
        when (view?.id) {
            R.id.rb_language -> {
                selectedPos = position
                (activity as LanguageActivity).languageObserver(selectedPos)
                notifyDataSetChanged()
            }
        }
    }
}
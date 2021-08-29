package com.webaddicted.kotlinproject.view.fragment

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowTextListBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility

class SpinnerListAdapter(private val mListBean: List<String>?) : BaseAdapter() {
    override fun getCount(): Int {
        return if (mListBean == null || mListBean.isEmpty()) 0 else mListBean.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val mBinding: RowTextListBinding?
        //        if (convertView == null) {
        mBinding = GlobalUtility.getLayoutBinding(
            parent?.context,
            R.layout.row_text_list
        ) as RowTextListBinding
        //            convertView.setTag(mBinding);
        //        } else
        //            mBinding = (RowTextListBinding) convertView.getTag();
        mBinding.txtName.text = mListBean!![position]
        return mBinding.root
    }
}

package com.webaddicted.kotlinproject.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.core.content.ContextCompat
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowTextListBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility

class ExpendableListAdapter(private var context: Context?) : BaseExpandableListAdapter() {
    private var parentArray = arrayOf("film", "game", "Application", "Wallpaper", "Theme")
    private var childArray = arrayOf(
        arrayOf("300", "DDLG", "Dshoom"),
        arrayOf("pirates of carrabian", "nfs", "hgjb"),
        arrayOf("300", "DDLG", "Dshoom"),
        arrayOf("pirates of carrabian", "nfs", "hgjb"),
        arrayOf("jgjgj", "jhkhkh", "jggkgk", "jhkh")
    )

    override fun getGroupCount(): Int {
        return parentArray.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return childArray[groupPosition].size
    }

    override fun getGroup(groupPosition: Int): Any {
        return groupPosition
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return childArray[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View {
        val mBinding: RowTextListBinding?
        //        if (convertView == null) {
        mBinding = GlobalUtility.getLayoutBinding(context, R.layout.row_text_list) as RowTextListBinding
//            LayoutInflater.from(parent.context),
//            R.layout.row_text_list,
//            parent, false
//        )
        //            convertView.setTag(mBinding);
        //        } else
        //            mBinding = (RowTextListBinding) convertView.getTag();
        mBinding.txtName.text = parentArray[groupPosition]
        mBinding.txtName.setPadding(2, 5, 5, 5)
        mBinding.card.setPadding(0, 2, 3, 5)
        return mBinding.root
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup?
    ): View {
        val mBinding: RowTextListBinding?
        //        if (convertView == null) {
        mBinding = GlobalUtility.getLayoutBinding(context, R.layout.row_text_list) as RowTextListBinding
        //            convertView.setTag(mBinding);
        //        } else
        //            mBinding = (RowTextListBinding) convertView.getTag();
        mBinding.txtName.text = childArray[groupPosition][childPosition]
        mBinding.txtName.setPadding(60, 5, 5, 5)
        mBinding.card.setPadding(60, 5, 2, 5)
//        mBinding!!.card.setCardElevation(0)
//        mBinding!!.card.setCardBackgroundColor(
//            mBinding!!.txtName.getContext().getResources().getColor(
//                R.color.transprant
//            )
//        )
        mBinding.root
            .setBackgroundColor(ContextCompat.getColor(context!!,R.color.green))
        return mBinding.root
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}

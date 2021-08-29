package com.webaddicted.kotlinproject.view.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowTextListBinding
import com.webaddicted.kotlinproject.view.base.BaseAdapter
import com.webaddicted.kotlinproject.view.fragment.TaskFrm
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Created by Deepak Sharma on 01/07/19.
 */
class TaskAdapter(private var taskFrm: TaskFrm, private var mTaskList: ArrayList<String>?) :
    BaseAdapter() {
    private var searchText: String? = null
    private val searchArray: List<String>

    init {
        this.searchArray = ArrayList()
        mTaskList?.let { this.searchArray.addAll(it) }
    }

    override fun getListSize(): Int {
        if (mTaskList == null) return 0
        return mTaskList?.size!!
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_text_list
    }

    override fun onBindTo(mRowBinding: ViewDataBinding, position: Int) {
        if (mRowBinding is RowTextListBinding) {
            val title = mTaskList?.get(position)
            if (searchText != null && searchText?.length!! > 1) {
                val sb = SpannableStringBuilder(title)
                val word: Pattern = Pattern.compile(searchText!!.toLowerCase())
                val match: Matcher = word.matcher(title!!.toLowerCase())
                while (match.find()) {
                    val fcs = ForegroundColorSpan(
                        ContextCompat.getColor(mContext, R.color.red)
                    )
                    sb.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
                mRowBinding.txtName.text = sb
            } else mRowBinding.txtName.text = title
            val rnd = Random()
            val color = Color.argb(225, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            (mRowBinding.txtInitial.background as GradientDrawable).setColor(color)
            mRowBinding.txtInitial.text = title?.get(0).toString()
            onClickListener(mRowBinding, mRowBinding.card, position)
        }
    }

    override fun getClickEvent(mRowBinding: ViewDataBinding, view: View?, position: Int) {
        super.getClickEvent(mRowBinding, view, position)
        when (view?.id) {
            R.id.card -> mTaskList?.get(position)?.let { taskFrm.onClicks(it) }
        }
    }

    fun notifyAdapter(list: ArrayList<String>) {
        this.mTaskList = list
        notifyDataSetChanged()
    }

    fun filter(textStr: String?) {
        val charText = textStr!!.toLowerCase(Locale.getDefault())
        searchText = charText
        mTaskList?.clear()
        if (charText == null && charText.isBlank()) {
            mTaskList?.addAll(searchArray)
        } else {
            for (wp in searchArray) {
                if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    mTaskList?.add(wp)
                }
                //                else if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                //                    mAction.add(wp);
                //                }
            }
        }
        notifyDataSetChanged()
    }
}

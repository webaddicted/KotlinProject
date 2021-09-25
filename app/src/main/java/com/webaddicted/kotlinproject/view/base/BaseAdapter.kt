package com.webaddicted.kotlinproject.view.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.webaddicted.kotlinproject.R

abstract class BaseAdapter:
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected lateinit var mContext: Context
    protected abstract fun getListSize(): Int?
    protected abstract fun getLayoutId(viewType: Int): Int
    protected abstract fun onBindTo(rowBinding: ViewDataBinding, position: Int)

    companion object {
        private val TAG = BaseAdapter::class.java.simpleName
    }

    override fun getItemCount(): Int {
        return getListSize() ?: 0
    }

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        mContext = parent.context
        val rowBindingUtil: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayoutId(viewType),
            parent,
            false
        )
        return ViewHolder(rowBindingUtil)
    }

    override fun onBindViewHolder(@NonNull holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder)
            holder.binding(position)
    }

    /**
     * placeholder type for image
     *
     * @param placeholderType position of string array placeholder
     * @return
     */
    protected open fun getPlaceHolder(placeholderType: Int): String {
        val placeholderArray = mContext.resources.getStringArray(R.array.image_loader)
        return placeholderArray[placeholderType]
    }

    /**
     * view holder
     */
    inner class ViewHolder(private val mRowBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(mRowBinding.root) {
        /**
         * @param position current item position
         */
        fun binding(position: Int) {
//            sometime adapter position  is -1 that case handle by position
            if (adapterPosition >= 0) onBindTo(mRowBinding, adapterPosition)
            else onBindTo(mRowBinding, position)
        }
    }

    protected open fun onClickListener(mRowBinding: ViewDataBinding, view: View?, position: Int) {
        view?.setOnClickListener { getClickEvent(mRowBinding, view, position) }
    }

    protected open fun getClickEvent(mRowBinding: ViewDataBinding, view: View?, position: Int) {

    }

}
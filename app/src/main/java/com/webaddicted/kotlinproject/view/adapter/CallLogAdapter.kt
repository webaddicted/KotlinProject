package com.webaddicted.kotlinproject.view.adapter

import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowContactBinding
import com.webaddicted.kotlinproject.global.calllog.LogObject
import com.webaddicted.kotlinproject.global.calllog.LogsManager
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.view.base.BaseAdapter
import java.text.DateFormat
import java.util.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class CallLogAdapter(private var list: MutableList<LogObject>?) : BaseAdapter() {
    override fun getListSize(): Int {
        if (list == null) return 0
        return list?.size!!
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_contact
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        if (rowBinding is RowContactBinding) {
            val source = list?.get(position)
            val date1 = Date(source?.date!!)
            val dateFormat: DateFormat =
                DateFormat.getDateTimeInstance(DateFormat.ERA_FIELD, DateFormat.SHORT)
            rowBinding.txtName.text = source.contactName
            rowBinding.txtNo.text = source.coolDuration
            rowBinding.txtEmail.text = dateFormat.format(date1)
            rowBinding.cb.gone()
            rowBinding.txtOtherInfo.gone()
            when (source.type) {
                LogsManager.INCOMING -> rowBinding.contactImage.setImageResource(R.drawable.call_received)
                LogsManager.OUTGOING -> rowBinding.contactImage.setImageResource(R.drawable.call_sent)
                LogsManager.MISSED -> rowBinding.contactImage.setImageResource(R.drawable.call_missed)
                else -> rowBinding.contactImage.setImageResource(R.drawable.call_cancelled)
            }
        }
    }

    fun notifyAdapter(prodList: MutableList<LogObject>?) {
        list = prodList
        notifyDataSetChanged()
    }
}
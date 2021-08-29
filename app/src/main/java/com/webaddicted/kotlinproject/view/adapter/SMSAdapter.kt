package com.webaddicted.kotlinproject.view.adapter

import android.text.Html
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowSmsBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility.Companion.getMilisecToDate
import com.webaddicted.kotlinproject.model.bean.common.SMSBean
import com.webaddicted.kotlinproject.view.base.BaseAdapter

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class SMSAdapter(private var list: java.util.ArrayList<SMSBean>?) : BaseAdapter() {
    override fun getListSize(): Int {
        if (list == null) return 0
        return list?.size!!
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_sms
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        if (rowBinding is RowSmsBinding) {
            val source = list?.get(position)
            rowBinding.txtTitle.text = source?.smsNo
            rowBinding.txtBody.text = source?.smsBody
            rowBinding.txtDate.text =  getMilisecToDate(source?.smsDate?.toLong()!!, "dd/MM/yyyy hh:mm:ss.SSS")
            rowBinding.txtStatus.text =source.smsStatus

            when {
                source.smsType.equals("1") -> {
                    rowBinding.txtType.text =
                        Html.fromHtml("<font color=\"#ff090b\">Inbox</font>", Html.FROM_HTML_MODE_LEGACY)
                    rowBinding.card.setCardBackgroundColor(mContext.getColor(R.color.inbox))
                }
                source.smsType.equals("0") -> {
                    rowBinding.txtType.text =
                        Html.fromHtml("<font color=\"#CDCDCD\">Sent</font>", Html.FROM_HTML_MODE_LEGACY)
                    rowBinding.card.setCardBackgroundColor(mContext.getColor(R.color.sent))
                }
                else -> rowBinding.txtType.text =
                    Html.fromHtml("<font color=\"#CDCDCD\">undefine</font>", Html.FROM_HTML_MODE_LEGACY)
            }
            if (source.smsType.equals("0"))
                rowBinding.txtRead.text =
                    Html.fromHtml("<font color=\"#ff090b\">Read : UNREAD</font>", Html.FROM_HTML_MODE_LEGACY)
            else rowBinding.txtRead.text =
                Html.fromHtml("<font color=\"#CDCDCD\">Read : READ</font>", Html.FROM_HTML_MODE_LEGACY)
            if (source.smsSeen.equals("0"))
                rowBinding.txtSeen.text =
                    Html.fromHtml("<font color=\"#ff090b\">Seen : UNSEEN</font>", Html.FROM_HTML_MODE_LEGACY)
            else rowBinding.txtSeen.text =
                Html.fromHtml("<font color=\"#CDCDCD\">Seen : SEEN</font>", Html.FROM_HTML_MODE_LEGACY)
        }
    }

    fun notifyAdapter(prodList: ArrayList<SMSBean>) {
        list = prodList
        notifyDataSetChanged()
    }
}
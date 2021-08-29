package com.webaddicted.kotlinproject.view.adapter

import android.text.Html
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowContactBinding
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.model.bean.common.ContactBean
import com.webaddicted.kotlinproject.view.base.BaseAdapter
import java.util.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class ContactAdapter(private var list: ArrayList<ContactBean>?) : BaseAdapter() {
    private var searchArray: ArrayList<ContactBean>

    init {
        this.searchArray = ArrayList()
        list?.let { this.searchArray.addAll(it) }
    }

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
            if (source?.contactName != null && source.contactName.isNotEmpty()) {
                rowBinding.txtName.visible()
                rowBinding.txtName.text = source.contactName
            } else rowBinding.txtName.gone()

            if (source?.contactEmail != null && source.contactEmail.isNotEmpty()) {
                rowBinding.txtEmail.visible()
                source.contactEmail=source.contactEmail.replace("\n","<br/>")
                rowBinding.txtEmail.text = Html.fromHtml("<font color=\"#000000\">Email Id</font></br/> \n${source.contactEmail}".trimIndent(), Html.FROM_HTML_MODE_LEGACY)
            } else rowBinding.txtEmail.gone()

            if (source?.contactNumber != null && source.contactNumber.isNotEmpty()) {
                rowBinding.txtNo.visible()
                source.contactNumber = source.contactNumber.replace("\n","<br/>")
                rowBinding.txtNo.text = Html.fromHtml("<font color=\"#000000\">Contact Number</font><br/> \n${source.contactNumber}".trimIndent(), Html.FROM_HTML_MODE_LEGACY)
            } else rowBinding.txtNo.gone()

            if (source?.contactInfo != null && source.contactInfo.isNotEmpty()) {
                rowBinding.txtOtherInfo.visible()
                source.contactInfo =  source.contactInfo.replace("\n","<br/>")
                rowBinding.txtOtherInfo.text = Html.fromHtml("<font color=\"#000000\">Other Details</font><br/> \n${source.contactInfo}".trimIndent(), Html.FROM_HTML_MODE_LEGACY)
            } else rowBinding.txtOtherInfo.gone()

            if (source?.checked === "0") {
                Lg.d(
                    "aaaaaaaaaaaaa",
                    "start page source.getChecked()==0   " + source
                        .checked + "  name===>" + source
                        .contactName
                )
                rowBinding.cb.isChecked = false
            } else {
                Lg.d(
                    "aaaaaaaaaaaaa",
                    "start page source.getChecked()==1   " + source
                        ?.checked.toString() + "   name===>" + source
                        ?.contactName
                )
                rowBinding.cb.isChecked = true
            }
            if (source?.contactPhoto != null)
                rowBinding.contactImage.setImageBitmap(source.contactPhoto)
            else
                rowBinding.contactImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.girl))
            onClickListener(rowBinding, rowBinding.cb, position)
        }
    }

    override fun getClickEvent(mRowBinding: ViewDataBinding, view: View?, position: Int) {
        super.getClickEvent(mRowBinding, view, position)
        mRowBinding as RowContactBinding
        val source = list?.get(position)
        when (view?.id) {
            R.id.cb -> {
                if (mRowBinding.cb.isChecked) {
                    source?.checked = "1"
                    Lg.d(
                        "aaaaaaaaaaaaa",
                        "checked ===> 1   " + source?.checked
                            .toString() + " " + source?.contactName
                    )
                } else {
                    source?.checked = "0"
                    Lg.d(
                        "aaaaaaaaaaaaa",
                        "checked ===> 0   " + source?.checked
                            .toString() + " " + source?.contactName
                    )
                }
            }
        }
    }

    fun filter(textStr: String?) {
        val charText = textStr!!.toLowerCase(Locale.getDefault())
        list?.clear()
        if (charText == null && !charText.isBlank()) {
            list?.addAll(searchArray)
        } else {
            for (wp in searchArray) {
                when {
                    wp.contactName .toLowerCase(Locale.getDefault()).contains(charText) ||
                            wp.contactNumber.toLowerCase(Locale.getDefault())
                                .contains(charText) -> {
                        list?.add(wp)
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    fun notifyAdapter(prodList: ArrayList<ContactBean>) {
        list = prodList
        searchArray = ArrayList()
        list?.let { searchArray.addAll(it) }
        notifyDataSetChanged()
    }
}
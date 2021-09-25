package com.webaddicted.kotlinproject.view.fragment

import android.Manifest
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmContactsBinding
import com.webaddicted.kotlinproject.global.common.PermissionHelper
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.model.bean.common.ContactBean
import com.webaddicted.kotlinproject.view.adapter.ContactAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import kotlinx.coroutines.*
import java.util.*


class ContactFrm : BaseFragment() {
    private lateinit var mAdapter: ContactAdapter
    private lateinit var mBinding: FrmContactsBinding
    private var contactList: ArrayList<ContactBean>? = ArrayList<ContactBean>()

    companion object {
        val TAG = ContactFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): ContactFrm {
            val fragment = ContactFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_contacts
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmContactsBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.contact_title)
        setAdapter()
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnSimpleContact.setOnClickListener(this)
        mBinding.btnCustomContact.setOnClickListener(this)
        mBinding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int, i1: Int, i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int, i1: Int, i2: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                val text: String =
                    mBinding.edtSearch.text.toString().toLowerCase(Locale.getDefault())
                mAdapter.filter(text)
            }
        })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_simple_contact -> checkContactPermission(false)
            R.id.btn_custom_contact ->
                checkContactPermission(true)
        }
    }

    private fun checkContactPermission(allContact: Boolean) {
        val locationList = ArrayList<String>()
        locationList.add(Manifest.permission.WRITE_CONTACTS)
        locationList.add(Manifest.permission.READ_CONTACTS)
        PermissionHelper.requestMultiplePermission(
            mActivity,
            locationList,
            object : PermissionHelper.Companion.PermissionListener {
                override fun onPermissionGranted(mCustomPermission: List<String>) {
                    if (allContact) {
                        showApiLoader()
                        GlobalScope.launch(Dispatchers.Main + Job()) {
                            withContext(Dispatchers.Default) {
                                contactList = readContacts()
                                hideApiLoader()
                            }
                            mBinding.includeContact.imgNoDataFound.gone()
                            mAdapter.notifyAdapter(contactList!!)
                        }
                    } else simpleContact()
                }

                override fun onPermissionDenied(mCustomPermission: List<String>) {
                }
            })
    }

    private fun setAdapter() {
        mAdapter = ContactAdapter(contactList)
        mBinding.includeContact.rvApps.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mBinding.includeContact.rvApps.adapter = mAdapter
    }

    private fun simpleContact() {
        val c: Cursor? = activity?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC "
        )
        contactList = ArrayList<ContactBean>()
        while (c?.moveToNext()!!) {
            val name =
                c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number =
                c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            contactList?.add(
                ContactBean().apply {
                    contactName = name
                    contactNumber = number
                    checked = "0"
                }
            )
        }
        c.close()
        mBinding.includeContact.imgNoDataFound.gone()
        mAdapter.notifyAdapter(contactList!!)
    }

    // Method that return all contact details in array format
    private fun readContacts(): ArrayList<ContactBean>? {
        val contactList: ArrayList<ContactBean> =
            ArrayList<ContactBean>()
        val uri =
            ContactsContract.Contacts.CONTENT_URI // Contact URI
        val contactsCursor: Cursor? = activity?.contentResolver?.query(
            uri, null, null,
            null, ContactsContract.Contacts.DISPLAY_NAME + " ASC "
        ) // Return
        // all
        // contacts
        // name
        // containing
        // in
        // URI
        // in
        // ascending
        // order
        // Move cursor at starting
        if (contactsCursor?.moveToFirst()!!) {
            do {
                val contctId = contactsCursor.getLong(
                    contactsCursor
                        .getColumnIndex("_ID")
                ) // Get contact ID
                val dataUri =
                    ContactsContract.Data.CONTENT_URI // URI to get
                // data of
                // contacts
                val dataCursor: Cursor? = activity?.contentResolver?.query(
                    dataUri, null,
                    ContactsContract.Data.CONTACT_ID + " = " + contctId,
                    null, null
                )
                // Strings to get all details
                var displayName = ""
                var nickName: String? = ""
                var homePhone: String? = ""
                var mobilePhone: String? = ""
                var workPhone: String? = ""
                var photoBitmap: Bitmap? = null
                var photoByte: ByteArray? = null // Byte to get photo since it will come
                // in BLOB
                var homeEmail: String? = ""
                var workEmail: String? = ""
                var companyName: String? = ""
                var title: String? = ""

                // This strings stores all contact numbers, email and other
                // details like nick name, company etc.
                val contactNumbers = StringBuilder()
                val contactEmailAddresses = StringBuilder()
                val contactOtherDetails = StringBuilder()
                // Now start the cusrsor
                if (dataCursor?.moveToFirst()!!) {
                    displayName =
                        dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    do {
                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")) == ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE) {
                            nickName =
                                dataCursor.getString(dataCursor.getColumnIndex("data1")) // Get Nick Name
                            if (nickName != null && nickName.isNotEmpty())
                                contactOtherDetails.append("NickName : ").append(nickName)
                                    .append("\n") // Add the nick name to string
                        }
                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")) == ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE) {
                            // In this get All contact numbers like home,
                            // mobile, work, etc and add them to numbers string
                            when (dataCursor.getInt(dataCursor.getColumnIndex("data2"))) {
                                ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> {
                                    homePhone =
                                        dataCursor.getString(dataCursor.getColumnIndex("data1"))
                                    if (homePhone != null && homePhone.isNotEmpty())
                                        contactNumbers.append("Home Phone : ").append(homePhone)
                                            .append("\n")
                                }
                                ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> {
                                    workPhone =
                                        dataCursor.getString(dataCursor.getColumnIndex("data1"))
                                    if (workPhone != null && workPhone.isNotEmpty())
                                        contactNumbers.append("Work Phone : ").append(workPhone)
                                            .append("\n")
                                }
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> {
                                    mobilePhone =
                                        dataCursor.getString(dataCursor.getColumnIndex("data1"))
                                    if (mobilePhone != null && mobilePhone.isNotEmpty())
                                        contactNumbers.append("Mobile Phone : ").append(mobilePhone)
                                            .append("\n")
                                }
                            }
                        }
                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")) == ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE) {
                            // In this get all Emails like home, work etc and
                            // add them to email string
                            when (dataCursor.getInt(dataCursor.getColumnIndex("data2"))) {
                                ContactsContract.CommonDataKinds.Email.TYPE_HOME -> {
                                    homeEmail =
                                        dataCursor.getString(dataCursor.getColumnIndex("data1"))
                                    if (homeEmail != null && homeEmail.isNotEmpty())
                                        contactEmailAddresses.append("Home Email : ")
                                            .append(homeEmail)
                                            .append("\n")
                                }
                                ContactsContract.CommonDataKinds.Email.TYPE_WORK -> {
                                    workEmail =
                                        dataCursor.getString(dataCursor.getColumnIndex("data1"))
                                    if (workEmail != null && workEmail.isNotEmpty())
                                        contactEmailAddresses.append("Work Email : ")
                                            .append(workEmail)
                                            .append("\n")
                                }
                            }
                        }
                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")) == ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE) {
                            companyName =
                                dataCursor.getString(dataCursor.getColumnIndex("data1")) // get company name
                            if (companyName != null && companyName.isNotEmpty())
                                contactOtherDetails.append("Company Name : ").append(companyName)
                                    .append("\n")
                            title = dataCursor.getString(dataCursor.getColumnIndex("data4"))
                            // get Company
                            // title
                            if (title != null && title.isNotEmpty())
                                contactOtherDetails.append("Title : ").append(title).append("\n")
                        }
                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")) == ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE) {
                            photoByte = dataCursor.getBlob(dataCursor.getColumnIndex("data15"))
                            if (photoByte != null) {
                                val bitmap =
                                    BitmapFactory.decodeByteArray(photoByte, 0, photoByte.size)
                                photoBitmap = bitmap
                            }
                        }
                    } while (dataCursor.moveToNext()) // Now move to next
                    // cursor
                    contactList.add(
                        ContactBean()
                            .apply {
                            contactId = contctId.toString()
                            contactName = displayName
                            contactNumber = contactNumbers.toString()
                            contactEmail = contactEmailAddresses.toString()
                            contactPhoto = photoBitmap
                            contactInfo = contactOtherDetails.toString()
                            checked = "0"
                        }
                    )
                }
            } while (contactsCursor.moveToNext())
        }
        return contactList
    }
}


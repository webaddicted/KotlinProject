package com.webaddicted.kotlinproject.view.fragment

import android.Manifest
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmPhoneImageBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.PermissionHelper
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.model.bean.common.ImagesBean
import com.webaddicted.kotlinproject.view.adapter.ImagesAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import kotlinx.coroutines.*


class PhoneImageFrm : BaseFragment(R.layout.frm_phone_image) {
    private lateinit var mAdapter: ImagesAdapter
    private lateinit var mBinding: FrmPhoneImageBinding
    private var imageBean: ArrayList<ImagesBean> = ArrayList()

    companion object {
        val TAG = PhoneImageFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): PhoneImageFrm {
            val fragment = PhoneImageFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmPhoneImageBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.phone_image_title)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnAllImage.setOnClickListener(this)
        setAdapter()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_all_image -> checkStoragePerm()
        }
    }

    private fun setAdapter() {
        mAdapter = ImagesAdapter(this, imageBean)
        mBinding.includeImage.rvApps.layoutManager = GridLayoutManager(activity, 3)
        mBinding.includeImage.rvApps.adapter = mAdapter
    }

    private fun checkStoragePerm() {
        val locationList = ArrayList<String>()
        locationList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        locationList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        PermissionHelper.requestMultiplePermission(
            mActivity,
            locationList,
            object : PermissionHelper.Companion.PermissionListener {
                override fun onPermissionGranted(mCustomPermission: List<String>) {
                    showApiLoader()
                    GlobalScope.launch(Dispatchers.Main + Job()) {
                        withContext(Dispatchers.Default) {
                            getAllImage()
                            hideApiLoader()
                        }
                        GlobalUtility.showToast("Total image are : ${imageBean.size}")
                        mBinding.includeImage.imgNoDataFound.gone()
                        mAdapter.notifyAdapter(imageBean)
                    }
                }

                override fun onPermissionDenied(mCustomPermission: List<String>) {
                }
            })
    }

    private fun getAllImage() {
        val columns = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN
        )
        val orderBy = MediaStore.Images.Media.DATE_TAKEN
        val cursor: Cursor = activity?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, "$orderBy DESC"
        )!!
        for (i in 0 until cursor.count) {
            cursor.moveToPosition(i)
            imageBean.add(
                ImagesBean().apply {
                imgId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID)).toString()
                imgName =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                        .toString()
                imgFolderName =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                        .toString()

                imgDate =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN))
                        .toString()
                image =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)).toString()
            })
        }
    }

    fun navigateScreen(tag: String?, path: String) {
        var frm: Fragment? = null
        when (tag) {
            ZoomImageFrm.TAG -> frm = ZoomImageFrm.getInstance(path, true)
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }

}


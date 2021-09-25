package com.webaddicted.kotlinproject.view.fragment

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmShareBinding
import com.webaddicted.kotlinproject.global.annotationdef.MediaPickerType
import com.webaddicted.kotlinproject.global.common.FileHelper
import com.webaddicted.kotlinproject.global.common.MediaPickerUtils
import com.webaddicted.kotlinproject.global.common.PermissionHelper
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.io.*
import java.net.URL
import java.net.URLConnection
import java.util.*

class ShareDataFrm : BaseFragment(R.layout.frm_share) {
    private lateinit var mBinding: FrmShareBinding

    companion object {
        val TAG = ShareDataFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): ShareDataFrm {
            val fragment = ShareDataFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmShareBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.share_title)

    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnShareText.setOnClickListener(this)
        mBinding.btnShareImage.setOnClickListener(this)
        mBinding.btnShareImgText.setOnClickListener(this)
        mBinding.btnShareLocalImage.setOnClickListener(this)
        mBinding.btnSendEmail.setOnClickListener(this)
        mBinding.btnOpenPdf.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_share_text -> shareText()
            R.id.btn_share_image -> chooseFile(false)
            R.id.btn_share_img_text -> chooseFile(true)
            R.id.btn_share_local_image -> shareLocalImage()
            R.id.btn_send_email -> sendEmail()
            R.id.btn_open_pdf -> checkPermission(this)
        }
    }

    private fun shareText() {
        val shareBody = "Here is the share content body"
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, resources.getString(R.string.share_text)))
    }

    private fun shareImage(file: File) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.toString()))
        startActivity(Intent.createChooser(share, resources.getString(R.string.share_image)))
    }

    private fun chooseFile(isShareImgText: Boolean) {
        mediaPicker.selectMediaOption(mActivity,
            MediaPickerType.SELECT_IMAGE,
            FileHelper.subFolder(),
            object : MediaPickerUtils.ImagePickerListener {
                override fun imagePath(filePath: List<File>) {
                    if (isShareImgText) shareImageText(filePath[0])
                    shareImage(filePath[0])
                }
            })
    }


    private fun shareImageText(file: File) {
        val b = FileHelper.getBitmapFromFile(file)
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/png"
        val bytes = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            activity?.contentResolver,
            b, "Title", null
        )
        val imageUri = Uri.parse(path)
        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        share.putExtra(Intent.EXTRA_STREAM, imageUri)
        share.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.dummyText) + "\nhttps://play.google.com/store/apps/details?id=com.solution.mircroprixs.tesseract&referrer=" + "TESSPOWER"
        )
        startActivity(Intent.createChooser(share, "Select"))
    }

    private fun shareLocalImage() {
        val b = BitmapFactory.decodeResource(resources, R.drawable.logo)
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/png"
        val bytes = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            activity?.contentResolver,
            b, "Title", null
        )
        val imageUri = Uri.parse(path)
        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        share.putExtra(Intent.EXTRA_STREAM, imageUri)
        share.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.dummyText) + "\nhttps://play.google.com/store/apps/details?id=com.solution.mircroprixs.tesseract&referrer=" + "TESSPOWER"
        )
        startActivity(Intent.createChooser(share, "Select"))
    }

    private fun sendEmail() {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "abc@gmail.com", null
            )
        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body")
        startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    private fun checkPermission(shareDataFrm: ShareDataFrm) {
        val locationList = ArrayList<String>()
        locationList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        locationList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        PermissionHelper.requestMultiplePermission(
            mActivity,
            locationList,
            object : PermissionHelper.Companion.PermissionListener {
                override fun onPermissionGranted(mCustomPermission: List<String>) {
                    FileHelper.createApplicationFolder()
                    val url = "http://housedocs.house.gov/edlabor/AAHCA-BillText-071409.pdf"
                    val file = File(FileHelper.subFolder(), FileHelper.getFileName(url))
                    if (file.exists()) FileHelper.openFile(mActivity, file)
                    else DownloadFile(shareDataFrm, url).execute()
                }

                override fun onPermissionDenied(mCustomPermission: List<String>) {

                }
            })
    }

    class DownloadFile(private var shareDataFrm: ShareDataFrm, var strUrl: String) :
        AsyncTask<String?, String?, String?>() {
        private var progressDialog: ProgressDialog? = null
        private var fileName: String? = null
        private var folder: String? = null

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(shareDataFrm.context)
            progressDialog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog?.setCancelable(false)
            progressDialog?.show()
        }

        /**
         * Downloading file in background thread
         */
//        override  fun doInBackground(vararg f_url: String): String {
        override fun doInBackground(vararg f_url: String?): String {
            var count: Long?
            try {
                val url = URL(strUrl)
                val connection: URLConnection = url.openConnection()
                connection.connect()
                // getting file length
                val lengthOfFile: Int = connection.contentLength
                // input stream to read file - with 8k buffer
                val input: InputStream = BufferedInputStream(url.openStream(), 8192)
                //Extract file name from URL
                fileName = FileHelper.getFileName(strUrl)
                //External directory path to save file
                folder = FileHelper.subFolder().path + "/"
                //Create androiddeft folder if it does not exist
                val directory = File(folder)
                if (!directory.exists()) {
                    directory.mkdirs()
                }
                // Output stream to write file
                val output: OutputStream = FileOutputStream(folder + fileName)
                val data = ByteArray(1024)
                var total: Long = 0
                while (input.read(data).also { count = it.toLong() } != -1) {
                    total += count!!
                    publishProgress("" + (total * 100 / lengthOfFile).toInt())
                    Log.d(TAG, "Progress: " + (total * 100 / lengthOfFile).toInt())
                    // writing data to file
                    output.write(data, 0, count?.toInt()!!)
                }
                // flushing output
                output.flush()
                // closing streams
                output.close()
                input.close()
                return "Downloaded at: $folder$fileName"
            } catch (e: Exception) {
                e.message?.let { Log.e("Error: ", it) }
            }
            return "Something went wrong"
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            progressDialog?.progress = values[0]?.toInt()!!
        }

        override fun onPostExecute(message: String?) {
            try {// dismiss the dialog after the file was downloaded
                if (progressDialog != null && progressDialog?.isShowing!!)
                    progressDialog?.dismiss()
                val file = File(FileHelper.appFolder(), FileHelper.getFileName(strUrl))
                if (file.exists()) FileHelper.openFile(shareDataFrm.mActivity, file)
                // Display File path after downloading
            } catch (exp: java.lang.Exception) {
                exp.printStackTrace()
            }
        }
    }


}


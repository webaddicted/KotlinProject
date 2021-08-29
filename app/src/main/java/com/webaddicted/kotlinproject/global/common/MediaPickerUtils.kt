package com.webaddicted.kotlinproject.global.common

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.global.annotationdef.MediaPickerType
import java.io.File
import java.util.*

class MediaPickerUtils {
    private var captureImageFile: File? = null
    private val TAG = MediaPickerUtils::class.java.simpleName
    val REQUEST_CAMERA_VIDEO = 5000
    val REQUEST_SELECT_FILE_FROM_GALLERY = 5002
    private var mImagePickerListener: ImagePickerListener? = null
    var mMimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
    var mImageMimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
    private var mfilePath: File? = null

    /**
     * select media option
     * @param activity
     * @param fileType
     * @param imagePickerListener
     */
    fun selectMediaOption(
        activity: Activity,
        @MediaPickerType.MediaType fileType: Int,
        filePath: File,
        imagePickerListener: ImagePickerListener
    ) {
        mImagePickerListener = imagePickerListener
        mfilePath = filePath
        checkPermission(activity, fileType)
    }

    /**
     * check file storage permission
     *
     * @param activity
     * @param fileType
     */
    private fun checkPermission(activity: Activity, @MediaPickerType.MediaType fileType: Int) {
        val locationList = ArrayList<String>()
        locationList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        locationList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        locationList.add(Manifest.permission.CAMERA)
        PermissionHelper.requestMultiplePermission(
            activity,
            locationList,
            object : PermissionHelper.Companion.PermissionListener {
                override fun onPermissionGranted(mCustomPermission: List<String>) {
                    FileHelper.createApplicationFolder()
                    selectMediaType(activity, fileType)
                }

                override fun onPermissionDenied(mCustomPermission: List<String>) {

                }
            })
    }

    private fun selectMediaType(activity: Activity, @MediaPickerType.MediaType fileType: Int) {
        val intent: Intent?
        when (fileType) {
            //            capture image for native camera
            MediaPickerType.CAPTURE_IMAGE -> {
                captureImageFile = FileHelper.createNewCaptureFile()
                 intent = FileHelper.getCaptureImageIntent(activity, captureImageFile)
                activity.startActivityForResult(intent, REQUEST_CAMERA_VIDEO)
            }
            //            pick image from gallery
            MediaPickerType.SELECT_IMAGE -> {
                intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mImageMimeTypes)
                intent.action = Intent.ACTION_GET_CONTENT
                activity.startActivityForResult(
                    Intent.createChooser(
                        intent,
                        activity.resources.getString(R.string.select_picture)
                    ), REQUEST_SELECT_FILE_FROM_GALLERY
                )
            }
            else -> throw IllegalStateException("Unexpected value: $fileType")
        }
    }

    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        var file: MutableList<File> = ArrayList()
        var compressedFiles: File?
        when (requestCode) {
            REQUEST_CAMERA_VIDEO -> {
                val bitmap: Bitmap?
                if (data?.extras != null) {
                    bitmap = data.extras!!.get("data") as Bitmap?
                    val originalFile = FileHelper.saveImage(bitmap, mfilePath)
                    file.add(originalFile)
                } else if (data?.data != null) {
                    // in case of record video
                    file = MediaPickerHelper().getData(activity, data)
                }
                if (file.size == 0) file.add(captureImageFile!!)
                mImagePickerListener!!.imagePath(file)
            }
            REQUEST_SELECT_FILE_FROM_GALLERY -> {
                val files = MediaPickerHelper().getData(activity, data)
                for (i in files.indices) {
                    val filePath = files[i].toString()
                    filePath.substring(filePath.lastIndexOf(".") + 1)
                    if (filePath.contains(mMimeTypes[0]) ||
                        filePath.contains(mMimeTypes[1]) ||
                        filePath.contains(mMimeTypes[2])
                    ) {
                        compressedFiles =
                                CompressImage.compressImage(activity, files[i].toString())
                        Lg.d(
                            TAG,
                            "old Image - ${FileHelper.formatSize(files[i].length())} \n " +
                                    "compress image - ${FileHelper.formatSize(compressedFiles.length())}"
                        )
                        files[i] = compressedFiles
                    }
                }
                mImagePickerListener!!.imagePath(files)
            }
        }
        refreshMedia(activity)
    }

    private fun refreshMedia(activity: Activity) {
        if (captureImageFile != null && captureImageFile!!.exists()) {
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = Uri.fromFile(captureImageFile)
            activity.sendBroadcast(intent)
        }
    }

    interface ImagePickerListener {
        fun imagePath(filePath: List<File>)
    }

}
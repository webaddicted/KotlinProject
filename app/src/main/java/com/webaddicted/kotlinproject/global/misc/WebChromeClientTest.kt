package com.webaddicted.kotlinproject.global.misc

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.view.activity.WebViewActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class WebChromeClientTest(private var myActivity: Activity?) :WebChromeClient(){
    // For Android 5.0+
    override fun onShowFileChooser(
        view: WebView,
        filePath: ValueCallback<Array<Uri>>,
        fileChooserParams: FileChooserParams
    ): Boolean {
        if (myActivity is WebViewActivity) {
            val webActivity = myActivity as WebViewActivity
            // Double check that we don't have any existing callbacks
            try {
                if (webActivity.mUploadMessage != null) {
                    webActivity.mUploadMessage?.onReceiveValue(null)
                }
            }catch (exp: Exception){
                Lg.d("TAG",""+exp)
            }
            webActivity.mUploadMessage = filePath
            Lg.e("FileCooserParams => ", filePath.toString())
            webActivity.mCameraPhotoPath = ""
            var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent!!.resolveActivity(webActivity.packageManager) != null) {
                // Create the File where the photo should go
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                    takePictureIntent.putExtra("PhotoPath", webActivity.mCameraPhotoPath)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    Lg.e("TAG", "Unable to create Image File $ex")
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    webActivity.mCameraPhotoPath = "file:" + photoFile.absolutePath
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                } else {
                    takePictureIntent = null
                }
            }

            val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
            contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            contentSelectionIntent.type = "image/*"

            val intentArray: Array<Intent?>
            intentArray = if (takePictureIntent != null) {
                arrayOf(takePictureIntent)
            } else {
                arrayOfNulls(2500)
            }

            val chooserIntent = Intent(Intent.ACTION_CHOOSER)
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
            webActivity.startActivityForResult(Intent.createChooser(chooserIntent, "Select images"), webActivity.INPUT_FILE_REQUEST_CODE)
        }
            return true
    }
        @Throws(IOException::class)
        private fun createImageFile(): File {
            // Create an image file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            )
            return File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
            )
        }

    }
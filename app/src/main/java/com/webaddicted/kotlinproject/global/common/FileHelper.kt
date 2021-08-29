package com.webaddicted.kotlinproject.global.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.itextpdf.text.pdf.PdfFileSpecification.url
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Deepak Sharma on 01/07/19.
 */
class FileHelper {
    companion object {
        private val APP_FOLDER = "kotlinProject"
        private val SUB_PROFILE = "/profile"
        private val SEPARATOR = "/"
        private val JPEG = ".jpeg"
        private val PNG = ".png"

        /**
         * This method is used to create application specific folder on filesystem
         */
        fun createApplicationFolder() {
            var f = File(
                Environment.getExternalStorageDirectory().toString(),
                File.separator + APP_FOLDER
            )
            f.mkdirs()
            f = File(
                Environment.getExternalStorageDirectory().toString(),
                File.separator + APP_FOLDER + SUB_PROFILE
            )
            f.mkdirs()
        }

        /**
         * Method to return file object
         *
         * @return File object
         */
        fun appFolder(): File {
            return File(
                Environment.getExternalStorageDirectory().toString(),
                File.separator + APP_FOLDER
            )
        }

        /**
         * Method to return file from sub folder
         *
         * @return File object
         */
        fun subFolder(): File {
            return File(
                Environment.getExternalStorageDirectory().toString(),
                File.separator + APP_FOLDER + SUB_PROFILE
            )
        }

        /**
         * Method to save bitmap
         *
         * @param bitmap
         * @return
         */
        fun saveBitmapImage(bitmap: Bitmap): File {
            val filename = System.currentTimeMillis().toString() + JPEG
            val dest = File(subFolder(), filename)
            try {
                val out = FileOutputStream(dest)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return dest
        }

        fun saveBitmapImageCache(activity: Activity, bitmap: Bitmap): File {
            val cacheDirectory: File = activity.baseContext?.filesDir!!
            val tmp =
                File(cacheDirectory.path + "/_androhub" + System.currentTimeMillis() + ".png")
            try {
                val fileOutputStream = FileOutputStream(tmp)
                bitmap.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    fileOutputStream
                )
                fileOutputStream.flush()
                fileOutputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return tmp
        }

        /**
         * Method to save bitmap
         *
         * @param bitmap
         * @param fileName
         * @return
         */
        fun saveBitmapImg(bitmap: Bitmap, fileName: String): File {
            var filename = System.currentTimeMillis().toString()
            filename += if (fileName.endsWith(".png"))
                PNG
            else
                JPEG
            val dest = File(appFolder(), filename)
            try {
                val out = FileOutputStream(dest)
                if (fileName.endsWith(".png"))
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
                else
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)

                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return dest
        }

        /**
         * Method to get filename from file
         *
         * @param file
         * @return
         */
        fun getFileName(file: File): String {
            return file.path.substring(file.path.lastIndexOf("/") + 1)
        }

        fun getFileName(url: String): String {
            return url.substring(url.lastIndexOf('/') + 1, url.length)
        }
        //    {START CAPTURE IMAGE PROCESS}
        /**
         * Method to create new file of captured image
         *
         * @return
         * @throws IOException
         */
        fun createNewCaptureFile(): File {
            val mFile = File(
                Environment.getExternalStorageDirectory().toString(),
                File.separator + APP_FOLDER + SUB_PROFILE + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg"
            )
            mFile.createNewFile()
            return mFile
        }

        /**
         *
         * Method to get Intent
         *
         * @param activity
         * @param photoFile
         * @return
         */
        fun getCaptureImageIntent(activity: Activity, photoFile: File?): Intent {
            var takePictureIntent: Intent? = null
            takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val photoURI = FileProvider.getUriForFile(
                activity,
                activity.packageName + ".provider",
                photoFile!!
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            return takePictureIntent
        }

        fun saveImage(image: Bitmap?, folderPath: File?): File {
            var savedImagePath: String? = null
            val timeStamp = SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
            ).format(Date())
            val imageFileName = "JPEG_$timeStamp.jpg"
            val imageFile = File(folderPath, imageFileName)
            savedImagePath = imageFile.absolutePath
            try {
                val fOut = FileOutputStream(imageFile)
                image?.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return imageFile
        }

        /**
         * Method to update phone gallery after capturing file
         *
         * @param context
         * @param imagePath
         */
        fun updateGallery(context: Context, imagePath: String?) {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(imagePath)
            val contentUri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            context.sendBroadcast(mediaScanIntent)
        }

        fun getBitmapFromFile(image: File): Bitmap {
            val bmOptions = BitmapFactory.Options()
            return BitmapFactory.decodeFile(image.absolutePath, bmOptions)
        }

        fun formatSize(size: Long): String {
            if (size <= 0)
                return "0"
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(
                size / Math.pow(
                    1024.0,
                    digitGroups.toDouble()
                )
            ) + " " + units[digitGroups]
        }

        fun calculatePercentage(value: Double, total: Double): Int {
            val usage: Double = (value * 100.0f / total)
            return usage.toInt()
        }
        fun openFile(activity: Activity, file: File) {
            var intent: Intent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    val uri =
                        FileProvider.getUriForFile(
                            activity,
                            activity.packageName + ".provider",
                            file
                        )
                    intent = Intent(Intent.ACTION_VIEW)
                    intent.data = uri
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    activity.startActivity(intent)
                } catch (exp: Exception) {
                    exp.printStackTrace()
                }
            } else {
                intent = Intent(Intent.ACTION_VIEW)
                val url = file.path
                val uri = Uri.fromFile(file)
                if (url.toString().contains(".doc") || url.toString().contains(".docx")) { // Word document
                    intent.setDataAndType(uri, "application/msword")
                } else if (url.toString().contains(".pdf")) { // PDF file
                    intent.setDataAndType(uri, "application/pdf")
                } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) { // Powerpoint file
                    intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
                } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) { // Excel file
                    intent.setDataAndType(uri, "application/vnd.ms-excel")
                } else if (url.toString().contains(".zip")) { // ZIP file
                    intent.setDataAndType(uri, "application/zip")
                } else if (url.toString().contains(".rar")) { // RAR file
                    intent.setDataAndType(uri, "application/x-rar-compressed")
                } else if (url.toString().contains(".rtf")) { // RTF file
                    intent.setDataAndType(uri, "application/rtf")
                } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) { // WAV audio file
                    intent.setDataAndType(uri, "audio/x-wav")
                } else if (url.toString().contains(".gif")) { // GIF file
                    intent.setDataAndType(uri, "image/gif")
                } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(
                        ".png"
                    )
                ) { // JPG file
                    intent.setDataAndType(uri, "image/jpeg")
                } else if (url.toString().contains(".txt")) { // Text file
                    intent.setDataAndType(uri, "text/plain")
                } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(
                        ".mp4"
                    ) || url.toString().contains(".avi")
                ) { // Video files
                    intent.setDataAndType(uri, "video/*")
                } else {
                    intent.setDataAndType(uri, "*/*")
                }
                intent.setDataAndType(Uri.parse(file.toString()), "application/pdf")
                intent = Intent.createChooser(intent, "Open File")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivity(intent)
            }
        }

    }

    //    {END CAPTURE IMAGE PROCESS}
}
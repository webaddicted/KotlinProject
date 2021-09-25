package com.webaddicted.kotlinproject.global.common

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class CompressImage {

    companion object {
        private val TAG = CompressImage::class.qualifiedName
        private var mContext: Context? = null
        private val imagePath = Environment.getExternalStorageDirectory().toString() + "/comp/"

        private val filename: String
            get() {
                val file = File(Environment.getExternalStorageDirectory().path, "/FirstFood")
                if (!file.exists()) {
                    file.mkdirs()
                }
                return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"

            }

        fun compressImage(context: Activity, imageUri: String): File {
            mContext = context
            //        String filePath = getRealPathFromURI(imageUri);
            var scaledBitmap: Bitmap? = null
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            var bmp = BitmapFactory.decodeFile(imageUri, options)
            var actualHeight = options.outHeight
            var actualWidth = options.outWidth
            val maxHeight = 816.0f
            val maxWidth = 612.0f
            var imgRatio = (actualWidth / actualHeight).toFloat()
            val maxRatio = maxWidth / maxHeight

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                } else {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()
                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
            options.inJustDecodeBounds = false
            options.inDither = false
            options.inPurgeable = true
            options.inInputShareable = true
            options.inTempStorage = ByteArray(16 * 1024)

            try {
                bmp = BitmapFactory.decodeFile(imageUri, options)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()

            }

            try {
                scaledBitmap =
                    Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }

            val ratioX = actualWidth / options.outWidth.toFloat()
            val ratioY = actualHeight / options.outHeight.toFloat()
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f

            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

            val canvas = Canvas(scaledBitmap!!)
            canvas.setMatrix(scaleMatrix)
            canvas.drawBitmap(
                bmp,
                middleX - bmp.width / 2,
                middleY - bmp.height / 2,
                Paint(Paint.FILTER_BITMAP_FLAG)
            )

            val exif: ExifInterface
            try {
                exif = ExifInterface(imageUri)

                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
                GlobalUtility.print(TAG, "Exif: $orientation")
                val matrix = Matrix()
                if (orientation == 6) {
                    matrix.postRotate(90f)
                    GlobalUtility.print(TAG, "Exif: $orientation")
                } else if (orientation == 3) {
                    matrix.postRotate(180f)
                    GlobalUtility.print(TAG, "Exif: $orientation")
                } else if (orientation == 8) {
                    matrix.postRotate(270f)
                    GlobalUtility.print(TAG, "Exif: $orientation")
                }
                scaledBitmap = Bitmap.createBitmap(
                    scaledBitmap,
                    0,
                    0,
                    scaledBitmap.width,
                    scaledBitmap.height,
                    matrix,
                    true
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return SaveImage(scaledBitmap!!)
        }

        private fun getRealPathFromURI(contentURI: String): String? {
            val contentUri = Uri.parse(contentURI)
            val cursor = mContext!!.contentResolver.query(contentUri, null, null, null, null)
            if (cursor == null) {
                return contentUri.path
            } else {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                return cursor.getString(idx)
            }
        }

        private fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            val totalPixels = (width * height).toFloat()
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }

            return inSampleSize
        }

        private fun SaveImage(finalBitmap: Bitmap): File {
            val dirFile: File
            GlobalUtility.print(TAG, "SaveImage: " + finalBitmap.toString().length)
            if (imagePath != null) {
                //            dirFile = new File(Environment.getExternalStorageDirectory() + imagePath);
                dirFile = FileHelper.subFolder()
            } else
                dirFile = File(mContext!!.cacheDir.toString())
            if (!dirFile.exists()) {
                dirFile.mkdirs()
            }
            val files = File(dirFile, System.currentTimeMillis().toString() + ".jpg")
            var fileOutputStream: FileOutputStream? = null
            try {
                files.createNewFile()
                fileOutputStream = FileOutputStream(files)
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
            } catch (fileNotFoundException: FileNotFoundException) {
                Log.e(TAG, "File not found!", fileNotFoundException)
            } catch (ioException: IOException) {
                TAG?.let { Lg.e(it, "Unable to write to file!", ioException) }
            }

            return files
        }
    }
}

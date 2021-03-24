package com.webaddicted.kotlinproject.global.common

import android.annotation.SuppressLint
import android.app.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.format.DateFormat
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.global.common.AppApplication.Companion.context
import com.webaddicted.kotlinproject.global.constant.AppConstant.Companion.NOTIFICATION_CHANNEL_ID
import com.webaddicted.kotlinproject.model.bean.common.NotificationData
import com.webaddicted.kotlinproject.view.activity.HomeActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Modifier
import java.net.InetAddress
import java.net.NetworkInterface
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class GlobalUtility {

    companion object {
        private var snackbar: Snackbar? = null
        fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun initSnackBar(context: Activity, networkConnected: Boolean) {
            if (networkConnected && snackbar != null && snackbar?.isShown!!) {
                updateSnackbar(snackbar!!)
                return
            }
            snackbar =
                Snackbar.make(
                    context.findViewById<View>(android.R.id.content),
                    "",
                    Snackbar.LENGTH_INDEFINITE
                )//.setBehavior(NoSwipeBehavior())
            snackbar?.let {
                val layoutParams =
                    (it.view.layoutParams as FrameLayout.LayoutParams)
                        .also { lp -> lp.setMargins(0, 0, 0, 0) }
                it.view.layoutParams = layoutParams
                it.view.alpha = 0.90f
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    it.view.elevation = 0f
                }
                val message = "no internet connection"
                if (networkConnected) updateSnackbar(it)
                else it.view.setBackgroundColor(Color.RED)
                val spannableString = SpannableString(message).apply {
                    setSpan(ForegroundColorSpan(Color.WHITE), 0, message.length, 0)
                }
                it.setText(spannableString)
                it.show()
            }
        }

        private fun updateSnackbar(view: Snackbar) {
            if (view != null) {
                val color = arrayOf(
                    ColorDrawable(Color.RED),
                    ColorDrawable(Color.GREEN)
                )
                val trans = TransitionDrawable(color)
                view.view.background = (trans)
                trans.startTransition(500)
                val handler = Handler()
                handler.postDelayed({ view.dismiss() }, 1300)
                view.setText("back online")
            }
        }

        fun getDate(context: Context, mDobEtm: TextView) {
            val datePickerDialog = DatePickerDialog(
                context,
                R.style.TimePicker,
                { view, year, month, dayOfMonth ->
                    var monthValue = month + 1
                    var day: String = ""
                    var dayMonth: String = ""
                    if (dayOfMonth <= 9) day = "0" + dayOfMonth
                    else day = dayOfMonth.toString()
                    if (monthValue <= 9) dayMonth = "0" + monthValue
                    else dayMonth = monthValue.toString()
                    mDobEtm.text =
                        "$day/$dayMonth/$year"
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePickerDialog.show()
        }

        fun getDOBDate(context: Context, mDobEtm: TextView) {
            val datePickerDialog =
                DatePickerDialog(
                    context,
                    R.style.TimePicker,
                    { view, year, month, dayOfMonth ->
                        var monthValue = month + 1
                        var day: String = ""
                        var dayMonth: String = ""

                        if (dayOfMonth <= 9) day = "0" + dayOfMonth
                        else day = dayOfMonth.toString()
                        if (monthValue <= 9) dayMonth = "0" + monthValue
                        else dayMonth = monthValue.toString()
                        mDobEtm.text =
                            "$dayMonth/$day/$year"
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DATE)
                )
            var calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -16)
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.show()
        }

        fun timePicker(
            activity: Activity,
            timeListener: TimePickerDialog.OnTimeSetListener
        ): TimePickerDialog {
            val calendar = Calendar.getInstance()
            return TimePickerDialog(
                activity,
                R.style.TimePicker,
                timeListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(context)
            )
        }

        /**
         * convert date formate
         *
         * @param date         date any formate string
         * @param inputFormat  input date formate
         * @param outputFormat output date formate
         * @return output date formate
         */
        fun dateFormate(date: String, inputFormat: String, outputFormat: String): String {
            var initDate: Date? = null
            try {
                initDate = SimpleDateFormat(inputFormat).parse(date)
            } catch (e: java.text.ParseException) {
                e.printStackTrace()
            }

            val formatter = SimpleDateFormat(outputFormat)
            return formatter.format(initDate)
        }

        fun getMilisecToDate(
            milliSeconds: Long,
            dateFormat: String?
        ): String? { // Create a DateFormatter object for displaying date in specified format.
            val formatter = SimpleDateFormat(dateFormat)
            // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }

        /**
         * convertTime formate
         *
         * @param timeHHMM
         * @return
         */
        fun timeFormat12(timeHHMM: String): String {
            try {
                val sdf = SimpleDateFormat("H:mm")
                val dateObj = sdf.parse(timeHHMM)

                return SimpleDateFormat("K:mm: a").format(dateObj)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }

        /**
         * convertTime formate
         *
         * @param timeHHMM
         * @return
         */
        fun timeFormat24(timeHHMM: String): String {
            val sdf = SimpleDateFormat("hh:mm a")
            var testDate: Date? = null
            try {
                testDate = sdf.parse(timeHHMM)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            val formatter = SimpleDateFormat("HH:MM")
            return formatter.format(testDate)
        }
//    {START HIDE SHOW KEYBOARD}

        /**
         * Method to hide keyboard
         *
         * @param activity Context of the calling class
         */
        fun hideKeyboard(activity: Activity) {
            try {
                val inputManager =
                    activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            } catch (ignored: Exception) {
                ignored.printStackTrace()
                Lg.d("TAG", "hideKeyboard: " + ignored.message)
            }

        }

        /***
         * Show SoftInput Keyboard
         * @param activity reference of current activity
         */
        fun showKeyboard(activity: Activity?) {
            if (activity != null) {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
            }
        }

//    {END HIDE SHOW KEYBOARD}


//      {START STRING TO JSON & JSON TO STRING}

        /**
         * @param json  json String converted by Gson to string
         * @param clazz referance of class type like MyBean.class
         * @param <T>
         * @return bean referance
        </T> */
        fun <T> stringToJson(json: String, clazz: Class<T>): T {
            return GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .create().fromJson(json, clazz)
        }

        /**
         * @param clazz referance of any bean
         * @return
         */
        fun jsonToString(clazz: Class<*>): String {
            return Gson().toJson(clazz)
        }

        //{END STRING TO JSON & JSON TO STRING}


        //block up when loder show on screen
        /**
         * handle ui
         *
         * @param activity
         * @param view
         * @param isBlockUi
         */
        fun handleUI(activity: Activity, view: View, isBlockUi: Boolean) {
            if (isBlockUi) {
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                view.visibility = View.VISIBLE
            } else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                view.visibility = View.GONE
            }
        }

        /**
         * provide binding of layout
         *
         * @param context reference of activity
         * @param layout  layout
         * @return viewBinding
         */
        fun getLayoutBinding(context: Context?, layout: Int): ViewDataBinding {
            return DataBindingUtil.inflate(
                LayoutInflater.from(context),
                layout,
                null, false
            )
        }

        /**
         * two digit random number
         *
         * @return random number
         */
        fun getTwoDigitRandomNo(): Int {
            return Random().nextInt(90) + 10
        }


        /**
         * show string in different color using spannable
         *
         * @param textView     view
         * @param txtSpannable string text
         * @param starText     start index of text
         * @param endText      end index of text
         */
        fun setSpannable(textView: TextView, txtSpannable: String, starText: Int, endText: Int) {
            val spannableString = SpannableString(txtSpannable)
            val foregroundSpan = ForegroundColorSpan(Color.GREEN)
            //            BackgroundColorSpan backgroundSpan = new BackgroundColorSpan(Color.GRAY);
            spannableString.setSpan(
                foregroundSpan,
                starText,
                endText,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            //            spannableString.setSpan(backgroundSpan, starText, endText, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.text = spannableString
        }

        /**
         * button click fade animation
         *
         * @param view view reference
         */
        fun btnClickAnimation(view: View) {
            val fadeAnimation = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
            view.startAnimation(fadeAnimation)
        }

        fun changeLanguage(context: Context, lancuageCode: String) {
//            val languageToLoad = "en" // your language
            val locale = Locale(lancuageCode)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            context.resources.updateConfiguration(
                config,
                context.resources.displayMetrics
            )
        }

        /**
         * Adds a watermark on the given image.
         */
        fun addWatermark(res: Resources, source: Bitmap, mScreenwidth: Int): Bitmap {
            val w: Int
            val h: Int
            val c: Canvas
            val paint: Paint
            val bmp: Bitmap
            val watermark: Bitmap
            val matrix: Matrix
            val scale: Float
            val r: RectF
            w = source.width
            h = source.height
            // Create the new bitmap
            bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)
            // Copy the original bitmap into the new one
            c = Canvas(bmp)
            c.drawBitmap(source, 0f, 0f, paint)
            // Load the watermark
            watermark = BitmapFactory.decodeResource(res, R.drawable.logo)
            // Scale the watermark to be approximately 40% of the source image height
            scale = (h.toFloat() * 0.15 / watermark.height.toFloat()).toFloat()
            val scaleX = w.toFloat() / watermark.width.toFloat()
            // Create the matrix
            matrix = Matrix()
            matrix.postScale(scaleX, scale)
            // Determine the post-scaled size of the watermark
            r = RectF(0f, 0f, watermark.width.toFloat(), watermark.height.toFloat())
            matrix.mapRect(r)
            // Move the watermark to the bottom right corner
            matrix.postTranslate(w - r.width(), h - r.height())
            // Draw the watermark
            c.drawBitmap(watermark, matrix, paint)
            // Free up the bitmap memory
            watermark.recycle()
            return bmp
        }

        fun showOfflineNotification(context: Context, title: String, description: String) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (intent != null) {
                val pendingIntent = PendingIntent.getActivity(
                    context, getTwoDigitRandomNo(), intent,
                    PendingIntent.FLAG_ONE_SHOT
                )
                val defaultSoundUri =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val notificationBuilder = NotificationCompat.Builder(context)
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
                notificationBuilder.setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.ic_launcher
                    )
                )
                notificationBuilder.setBadgeIconType(R.mipmap.ic_launcher_round)
                notificationBuilder.setContentTitle(title)
                if (description != null) {
                    notificationBuilder.setContentText(description)
                    notificationBuilder.setStyle(
                        NotificationCompat.BigTextStyle().bigText(description)
                    )
                }
                notificationBuilder.setAutoCancel(true)
                notificationBuilder.setSound(defaultSoundUri)
                notificationBuilder.setVibrate(longArrayOf(1000, 1000))
                notificationBuilder.setContentIntent(pendingIntent)
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val importance = NotificationManager.IMPORTANCE_HIGH
                    val notificationChannel = NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        "NOTIFICATION_CHANNEL_NAME",
                        importance
                    )
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.RED
                    notificationChannel.enableVibration(true)
                    notificationChannel.vibrationPattern = longArrayOf(1000, 1000)
                    assert(notificationManager != null)
                    notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
                    notificationManager.createNotificationChannel(notificationChannel)
                }
                notificationManager.notify(
                    getTwoDigitRandomNo()/*Id of Notification*/,
                    notificationBuilder.build()
                )
            }
        }

        fun getIntentForPush(
            ctx: Context,
            mNotificationData: NotificationData?
        ): Intent? {
            var mIntent: Intent? = null
            if (mNotificationData != null) {
//                if (mPrefMgr.getUserId() != null && !mPrefMgr.getUserId().isEmpty()) {
                mIntent = Intent(ctx, HomeActivity::class.java)
//                    mIntent.putExtra(
//                        AppConstant.NOTIFICATION_GAME_ID,
//                        String.valueOf(mNotificationData!!.getId())
//                    )
//                    mIntent.putExtra(AppConstant.NOTIFICATION_CODE, mNotificationData!!.getType())
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                }
            }
            return mIntent
        }

        fun captureScreen(activity: Activity) {
            val v: View = activity.window.decorView.rootView
            v.isDrawingCacheEnabled = true
            val bmp: Bitmap = Bitmap.createBitmap(v.drawingCache)
            v.isDrawingCacheEnabled = false
            try {
                val sd = FileHelper.appFolder()
                val dest = File(
                    sd, "SCREEN"
                            + System.currentTimeMillis() + ".png"
                )
                val fos = FileOutputStream(dest)
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun isWifiConnected(activity: Activity): String? {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) { // connected to wifi
                    return activity.resources
                        .getString(R.string.wifi)
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) { // connected to the mobile provider's data plan
                    return activity.resources.getString(R.string.network)
                }
            } else return activity.resources.getString(R.string.unavailable)
            return ""
        }

        /**
         * Get IP address from first non-localhost interface
         *
         * @param useIPv4 true=return ipv4, false=return ipv6
         * @return address or empty string
         */
        fun getIPAddress(useIPv4: Boolean): String? {
            try {
                val interfaces: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (intf in interfaces) {
                    val addrs: List<InetAddress> =
                        Collections.list(intf.inetAddresses)
                    for (addr in addrs) {
                        if (!addr.isLoopbackAddress) {
                            val sAddr = addr.hostAddress
                            //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            val isIPv4 = sAddr.indexOf(':') < 0
                            if (useIPv4) {
                                if (isIPv4) return sAddr
                            } else {
                                if (!isIPv4) {
                                    val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                    return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                        0,
                                        delim
                                    ).toUpperCase()
                                }
                            }
                        }
                    }
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
            return ""
        }

        fun getMACAddress(interfaceName: String?): String? {
            try {
                val interfaces: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (intf in interfaces) {
                    if (interfaceName != null) {
                        if (!intf.name.equals(interfaceName, ignoreCase = true)) continue
                    }
                    val mac = intf.hardwareAddress ?: return ""
                    val buf = java.lang.StringBuilder()
                    for (aMac in mac) buf.append(String.format("%02X:", aMac))
                    if (buf.length > 0) buf.deleteCharAt(buf.length - 1)
                    return buf.toString()
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
            return ""
        }

        fun pxToDp(mainActivity: Activity, px: Int): Int {
            val displayMetrics: DisplayMetrics =
                mainActivity.resources.displayMetrics
            return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        }

        fun getDate(timeStamp: Long): String? {
            return try {
                @SuppressLint("SimpleDateFormat") val sdf: java.text.DateFormat =
                    SimpleDateFormat("dd MMM yyyy HH:mm:ss z")
                val netDate = Date(timeStamp)
                sdf.format(netDate)
            } catch (ex: java.lang.Exception) {
                "xx"
            }
        }

        /***
         * To prevent from double clicking the row item and so prevents overlapping fragment.
         */
        fun avoidDoubleClicks(view: View) {
            val DELAY_IN_MS: Long = 500
            if (!view.isClickable) {
                return
            }
            view.isClickable = false
            view.postDelayed({ view.isClickable = true }, DELAY_IN_MS)
        }

        fun rateUsApp(mActivity: Activity) {
//            var packageName= "com.quixom.deviceinfo"
            val packageName = mActivity.packageName
            val uri = Uri.parse("market://details?id=$packageName")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                mActivity.startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                mActivity.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }
         fun setEnableView(edtView: TextInputEditText, isViewEditable: Boolean) {
            if (isViewEditable) {
                edtView.isClickable = true
                edtView.isLongClickable = true
                edtView.isFocusableInTouchMode = true
                edtView.setTextColor(edtView.context.getColor(R.color.black))
            } else {
                edtView.isClickable = false
                edtView.isLongClickable = false
                edtView.isFocusableInTouchMode = false
                edtView.setTextColor(edtView.context.getColor(R.color.gray))
            }
        }
        @SuppressLint("MissingPermission")
        fun getDeviceIMEI(activity: Activity): String? {
//            var tm = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            return tm.deviceId
            return Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID).toString()
//            return "868494034542635"
        }
    }


}

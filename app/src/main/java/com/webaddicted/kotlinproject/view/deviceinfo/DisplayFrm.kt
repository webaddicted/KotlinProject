package com.webaddicted.kotlinproject.view.deviceinfo

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmDevDisplayBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.sqrt

class DisplayFrm : BaseFragment() {
    private lateinit var mBinding: FrmDevDisplayBinding
    val dm = DisplayMetrics()

    companion object {
        val TAG = DisplayFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): DisplayFrm {
            val fragment = DisplayFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_dev_display
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmDevDisplayBinding
        getDisplayInfo()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun getDisplayInfo() {
        var txtColor = ""
        if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_NO
        ) txtColor = "#000000"
        else txtColor = "#FFFFFF"
        var screenInfo: String
        var displayInfo = ""
        var resoluationInfo= ""

        val display =
            (activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)

        /*** Screen Size */
        val screenSizes: String =
            when (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
                Configuration.SCREENLAYOUT_SIZE_LARGE -> "Large screen"
                Configuration.SCREENLAYOUT_SIZE_NORMAL -> "Normal screen"
                Configuration.SCREENLAYOUT_SIZE_SMALL -> "Small screen"
                else -> "Screen size is neither large, normal or small"
            }
        screenInfo = "<font color=\"$txtColor\">Screen Size : </font>${screenSizes}<br>"
        /*** Screen physical size */
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)

        val realSize = Point()
        Display::class.java.getMethod("getRealSize", Point::class.java).invoke(display, realSize)
        val pWidth = realSize.x
        val pHeight = realSize.y
        val wi = pWidth.toDouble() / dm.xdpi.toDouble()
        val hi = pHeight.toDouble() / dm.ydpi.toDouble()
        val x = wi.pow(2.0)
        val y = hi.pow(2.0)
        val screenInches = sqrt(x + y)
        screenInfo += "<font color=\"$txtColor\">Physical Size : </font>${returnToDecimalPlaces(
            screenInches
        ).plus(resources.getString(R.string.inches))}<br>"

        /*** Screen default orientation */
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT ->
                screenInfo += "<font color=\"$txtColor\">Default Orientation : </font>${resources.getString(
                    R.string.orientation_portrait
                )}<br>"
            Configuration.ORIENTATION_LANDSCAPE ->
                screenInfo += "<font color=\"$txtColor\">Default Orientation : </font>${resources.getString(
                    R.string.orientation_landscape
                )}<br>"
            Configuration.ORIENTATION_UNDEFINED ->
                screenInfo += "<font color=\"$txtColor\">Default Orientation : </font>${resources.getString(
                    R.string.orientation_undefined
                )}<br>"
        }

        /*** Display screen width and height */
        screenInfo += "<font color=\"$txtColor\">Screen Width : </font>${pWidth.toString().plus(
            resources.getString(R.string.px)
        )}<br>"
        screenInfo += "<font color=\"$txtColor\">Screen Height : </font>${pHeight.toString().plus(
            resources.getString(R.string.px)
        )}<br>"
        /*** Screen refresh rate */
        screenInfo += "<font color=\"$txtColor\">Refresh Rate : </font>${display.refreshRate.toString().plus(
            resources.getString(R.string.fps)
        )}<br>"
        /*** Display name */
        screenInfo += "<font color=\"$txtColor\">Screen Name : </font>${display.name}<br>"
        mBinding.txtScreenInfo.text = Html.fromHtml(screenInfo, Html.FROM_HTML_MODE_LEGACY)

        /*** Max GPU Texture size */

        /* Handler().postDelayed({
             val gpu = GPU(mActivity)
             gpu.loadOpenGLGles10Info { result -> result.toString()
                 tvMaxGpuSize?.text = result.GL_MAX_TEXTURE_SIZE.toString().plus(mResources.getString(R.string.x)).plus(result.GL_MAX_TEXTURE_SIZE)
             }
         }, 1000)*/

        /*** Screen Display buckets */
        when (resources.displayMetrics.density) {
            .75f ->
                displayInfo =
                    "<font color=\"$txtColor\">Display bucket : </font>${resources.getString(R.string.ldpi)}<br>"
            1.0f ->
                displayInfo =
                    "<font color=\"$txtColor\">Display bucket : </font>${resources.getString(R.string.mdpi)}<br>"
            1.5f ->
                displayInfo =
                    "<font color=\"$txtColor\">Display bucket : </font>${resources.getString(R.string.hdpi)}<br>"
            2.0f ->
                displayInfo =
                    "<font color=\"$txtColor\">Display bucket : </font>${resources.getString(R.string.xhdpi)}<br>"
            3.0f ->
                displayInfo =
                    "<font color=\"$txtColor\">Display bucket : </font>${resources.getString(R.string.xxhdpi)}<br>"
            4.0f ->
                displayInfo =
                    "<font color=\"$txtColor\">Display bucket : </font>${resources.getString(R.string.xxxhdpi)}<br>"
        }

        /*** Screen Dpi */
        displayInfo += "<font color=\"$txtColor\">Display dpi : </font>${resources.displayMetrics.densityDpi.toString().plus(
            resources.getString(R.string.dpi)
        )}<br>"
        /*** Screen xDpi and yDpi */
        displayInfo += "<font color=\"$txtColor\">XDpi : </font>${dm.xdpi.toString().plus(resources.getString(R.string.dpi))}<br>"
        displayInfo += "<font color=\"$txtColor\">YDpi : </font>${dm.ydpi.toString().plus(resources.getString(R.string.dpi))}<br>"
        /*** Screen logical density */
        displayInfo += "<font color=\"$txtColor\">Logical Density : </font>${dm.density}<br>"
        /*** Screen scaled density */
        displayInfo += "<font color=\"$txtColor\">Scaled Density : </font>${dm.scaledDensity}<br>"
        mBinding.txtDisplayInfo.text = Html.fromHtml(displayInfo, Html.FROM_HTML_MODE_LEGACY)

        /*** Screen usable width and height */
        val size = Point()
        display.getSize(size)
        resoluationInfo += "<font color=\"$txtColor\">Usable Width : </font>${size.x.toString().plus(resources.getString(R.string.px))}<br>"
        resoluationInfo += "<font color=\"$txtColor\">Usable Height : </font>${size.y.toString().plus(resources.getString(R.string.px))}<br>"

        /*** Screen density independent width and height */
        resoluationInfo += "<font color=\"$txtColor\">Display Independent Width : </font>${GlobalUtility.pxToDp(activity!!, dm.widthPixels).toString()
            .plus(resources.getString(R.string.dp))}<br>"
        resoluationInfo += "<font color=\"$txtColor\">Display Independent Height : </font>${GlobalUtility.pxToDp(activity!!, dm.heightPixels).toString()
            .plus(resources.getString(R.string.dp))}<br>"
        mBinding.txtResolInfo.text = Html.fromHtml(resoluationInfo, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun returnToDecimalPlaces(values: Double): String {
        val df = DecimalFormat("#.00")
        return df.format(values)
    }
}

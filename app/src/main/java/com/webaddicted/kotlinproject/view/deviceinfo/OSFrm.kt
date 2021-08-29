package com.webaddicted.kotlinproject.view.deviceinfo

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmDevOsBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.view.base.BaseFragment

class OSFrm : BaseFragment() {
    private lateinit var mBinding: FrmDevOsBinding

    companion object {
        val TAG = OSFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): OSFrm {
            val fragment = OSFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_dev_os
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmDevOsBinding
        getOSInfo()
    }

    private fun getOSInfo() {
        var txtColor = ""
        if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_NO
        ) txtColor = "#000000"
        else txtColor = "#FFFFFF"
        val CVersion = Build.VERSION.SDK_INT
        var osInfo = ""
        osInfo =   "<font color=\"$txtColor\">Version : </font>${Build.VERSION.RELEASE}<br>" +
                "<font color=\"$txtColor\">Version Name : </font>${Build.VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT].name}<br>" +
                "<font color=\"$txtColor\">Api Level : </font>${Build.VERSION.SDK_INT}<br>" +
                "<font color=\"$txtColor\">BuildId : </font>${Build.ID}<br>" +
                "<font color=\"$txtColor\">Build Time : </font>${GlobalUtility.getDate(Build.TIME)}<br>" +
                "<font color=\"$txtColor\">Fingerprint : </font>${Build.FINGERPRINT}<br>" +
                "<font color=\"$txtColor\">Hardware : </font>${Build.HARDWARE}<br>"
        mBinding.txtOsInfo.text = Html.fromHtml(osInfo, Html.FROM_HTML_MODE_LEGACY)
        when (CVersion) {
            11 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.honeycomb) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("February 22, 2011")
            }
            12 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.honeycomb) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("May 10, 2011")
            }
            13 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.honeycomb) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("July 15, 2011")
            }
            14 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.ics) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("October 18, 2011")
            }
            15 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.ics) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("November 28, 2011")
            }
            16 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.jellybean) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("July 9, 2012")
            }
            17 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.jellybean) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("November 13, 2012")
            }
            18 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.jellybean) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("July 24, 2013")
            }
            19 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.kitkat) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("October 31, 2013")
            }
            21 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.lollipop) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("November 12, 2014")
            }
            22 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.lollipop) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("March 9, 2015")
            }
            23 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.marshmallow) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("October 5, 2015")
            }
            24 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.nougat) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("August 22, 2016")
            }
            25 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.nougat) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("October 4, 2016")
            }
            26 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.oreo) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("August 21, 2017")
            }
            27 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.oreo) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("December 15, 2017")
            }
            28 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.pie) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("August 6, 2018")
            }
            29 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.q) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("September 3, 2019")
            }
            30 -> {
                mBinding.txtVersion.text =
                    resources.getString(R.string.r) + (" " + Build.VERSION.RELEASE.toString())
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("August 21, 2017")
            }
            else -> {
                mBinding.txtVersion.text = resources.getString(R.string.unknown_version)
                mBinding.txtReleaseDate.text = resources.getString(R.string.release_date) + ("-")
            }
        }

        //        deviceInfoList.get(0).setBuildRelease(Build.VERSION.RELEASE);
//        deviceInfoList.get(0).setDisplay(Build.DISPLAY);
//        deviceInfoList.get(0).setFingerprint(Build.FINGERPRINT);
//        deviceInfoList.get(0).setBuildId(Build.ID);
//        deviceInfoList.get(0).setTime(String.valueOf(Build.TIME));
//        deviceInfoList.get(0).setType(Build.TYPE);
//        deviceInfoList.get(0).setUser(Build.USER);
//        deviceInfoList.get(0).setVersion(String.valueOf(Build.VERSION.SDK_INT));
    }

}

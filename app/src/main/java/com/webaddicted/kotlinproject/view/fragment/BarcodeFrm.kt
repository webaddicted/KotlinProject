package com.webaddicted.kotlinproject.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmBarcodeBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment
import org.json.JSONException
import org.json.JSONObject


class BarcodeFrm : BaseFragment() {
    private lateinit var mBinding: FrmBarcodeBinding
    private var qrScan: IntentIntegrator? = null

    companion object {
        val TAG = BarcodeFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): BarcodeFrm {
            val fragment = BarcodeFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_barcode
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmBarcodeBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.barcode)
        qrScan = IntentIntegrator(activity)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnScanBarcode.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_scan_barcode -> qrScan!!.initiateScan()
        }
    }

     override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            //if qrcode has nothing in it
            mBinding.txtScanData.text = result.contents
            if (result.contents == null) GlobalUtility.showToast("Result Not Found")
            else {
                //if qr contains data
                try {
                    //converting the data to json
                    val obj = JSONObject(result.contents)
                    GlobalUtility.showToast(obj.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast

                    GlobalUtility.showToast(result.contents)
                    if (result.contents.contains("http") || result.contents.contains("com")) {
                        goWebSite(result.contents)
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun goWebSite(content: String?) {
//        Toast.makeText(this, "2"+content, Toast.LENGTH_SHORT).show();
        mBinding.webview.settings.javaScriptEnabled = true
        //web.getSettings().getAllowUniversalAccessFromFileURLs();
        mBinding.webview.settings.loadWithOverviewMode = true
        mBinding.webview.settings.useWideViewPort = true
//        mBinding.webview.setWebViewClient(ourViewClient())
        try {
            content?.let { mBinding.webview.loadUrl(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


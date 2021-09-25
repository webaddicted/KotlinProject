package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityWebviewBinding
import com.webaddicted.kotlinproject.global.common.*
import com.webaddicted.kotlinproject.global.misc.WebChromeClientTest
import com.webaddicted.kotlinproject.view.base.BaseActivity


class WebViewActivity : BaseActivity(R.layout.activity_webview) {
    private var url: String = "http://www.google.com"
    private lateinit var mBinding: ActivityWebviewBinding
    val INPUT_FILE_REQUEST_CODE = 1
    var mUploadMessage: ValueCallback<Array<Uri>>? = null
    var mCameraPhotoPath: String = ""

    companion object {
        val TAG = WebViewActivity::class.qualifiedName
        fun newIntent(context: Context) {
            context.startActivity(Intent(context, WebViewActivity::class.java))
        }
    }

    override fun onBindTo(binding: ViewDataBinding) {
        mBinding = binding as ActivityWebviewBinding
        init()
        clickListener()
        checkStoragePermission()
        normalWebView(mBinding.webview)
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.webview_title)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnNormalWebview.setOnClickListener(this)
        mBinding.btnWebClick.setOnClickListener(this)
        mBinding.btnWebFileChoose.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> onBackPressed()
            R.id.btn_normal_webview -> normalWebView(mBinding.webview)
            R.id.btn_web_click -> webInterface(mBinding.webview)
            R.id.btn_web_file_choose -> chooseImageWebViewUrl(
                this,
                mBinding.webview,
                "https://en.imgbb.com/"
            )
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.mBinding.webview.canGoBack()) {
            this.mBinding.webview.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun normalWebView(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.isScrollbarFadingEnabled = true
        webView.isVerticalScrollBarEnabled = false
        webView.webViewClient = myTestBrowser()
        try {
            webView.loadUrl(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun webInterface(webView: WebView) {
        mBinding.toolbar.imgBack.visibility = View.VISIBLE
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.webview_title)
        webView.settings.loadsImagesAutomatically = true
//        webView.settings.setUserAgentString("android");
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.isScrollbarFadingEnabled = true
        webView.isVerticalScrollBarEnabled = false
        webView.addJavascriptInterface(
            WebAppInterface(
                this
            ), "android"
        )
        webView.webViewClient = myTestBrowser()
        val str = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "<h2>Sample HTML Page</h2>\n" +
                "<input type=\"button\" value=\"Say hello\" onClick=\"showAndroidToast('Hello Android!')\" />\n" +
                "<script type=\"text/javascript\">\n" +
                "    function showAndroidToast(toast) {\n" +
                "        android.showToast(toast);\n" +
                "    }\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>"
        webView.loadData(str, "text/html", "utf-8")
    }

    private inner class myTestBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            mBinding.progressBar.visibility = View.VISIBLE
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            mBinding.progressBar.visibility = View.GONE
            super.onPageFinished(view, url)
        }
    }

    /** Instantiate the interface and set the context  */
    class WebAppInterface(private val mContext: Context) {
        /** Show a toast from the web page  */
        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * show response url on webview
     *
     * @param webView    view
     * @param contentUrl webview url
     */
    private fun chooseImageWebViewUrl(activity: Activity?, webView: WebView, contentUrl: String?) {
        try {
            val cookieManager = CookieManager.getInstance()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.removeAllCookies(ValueCallback<Boolean> { aBoolean ->
                    // a callback which is executed when the cookies have been removed
//                    Log.d(FragmentActivity.TAG, "Cookie removed: " + aBoolean!!)
                })
            } else
                cookieManager.removeAllCookie()
            webView.settings.javaScriptEnabled = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = true
            webView.isScrollbarFadingEnabled = true
            webView.isVerticalScrollBarEnabled = false
            webView.settings.userAgentString = "android"
            webView.settings.setAppCacheEnabled(false)
            webView.settings.domStorageEnabled = true
            webView.clearCache(true)
            webView.settings.pluginState = WebSettings.PluginState.ON
            webView.settings.setSupportZoom(true)
            webView.settings.allowFileAccess = (true)
            webView.settings.allowContentAccess = (true)
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            //            webView.getSettings().Access = (true);
            webView.webChromeClient = WebChromeClientTest(activity)
            webView.webViewClient = myTestBrowser()
            contentUrl?.let { webView.loadUrl(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mUploadMessage == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        try {
            if (resultCode == 0) {
                GlobalUtility.print(TAG, "when user cancel then reset web chrome client")
                mBinding.webview.webChromeClient = WebChromeClientTest(this)
                mUploadMessage?.onReceiveValue(arrayOf(Uri.parse("")))
                return
            }
            if (data?.data != null || data?.clipData != null) {
                val files = MediaPickerHelper().getData(this, data)
                val arrayLists = Array(files.size) { i -> Uri.fromFile(files[i]) }
                showToast("selected image size - " + files.size)
                mUploadMessage?.onReceiveValue(arrayLists)
            } else {
                mUploadMessage?.onReceiveValue(arrayOf(Uri.parse(mCameraPhotoPath)))
                showToast("captured image - $mCameraPhotoPath")
            }
        } catch (e: Exception) {
            Lg.e("Error!", "Error while opening image file: ${e.localizedMessage}")
            mUploadMessage?.onReceiveValue(arrayOf(Uri.parse("")))
            mUploadMessage = null
        }
        mUploadMessage = null
    }
}


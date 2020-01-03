package com.fan.weblib

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

class UniversalWebView(context: Context, attrs: AttributeSet) : WebView(context, attrs) {

    private var mUrl = ""

    fun load(url: String) {
        mUrl = url
        if (url.endsWith(".pdf")) {
            loadPdf()
        } else if (mUrl.endsWith(".jpg") || mUrl.endsWith(".png")) {
            loadImage()
        } else if (url.contains(".doc") || url.contains(".docx")) {
            loadWord()
        } else {
            loadUrl(url)
        }
    }

    /**
     * 加载图片
     * 默认自适应全屏
     */
    private fun loadImage(selfAdaption: Boolean = true, widthPercent: String = "100") {
        val useStyle = if (selfAdaption) "style='max-width:$widthPercent%;height:auto';" else ""
        loadDataWithBaseURL(
            null,
            "<center><img $useStyle src=$mUrl /></center>",
            "text/html",
            "charset=UTF-8",
            null
        )
    }

    /**
     * 加载PDF
     */
    private fun loadPdf() {
        settings.javaScriptEnabled = true
        settings.allowFileAccess = true
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=$mUrl")
    }

    /**
     * 使用office官方的在线加载word，
     * 缺点：无法使用ip请求，只能用域名
     */
    private fun loadWord() {
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccessFromFileURLs = true
        loadUrl("https://view.officeapps.live.com/op/view.aspx?src=$mUrl")
    }
}
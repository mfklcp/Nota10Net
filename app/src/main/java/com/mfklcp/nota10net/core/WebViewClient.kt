package com.mfklcp.nota10net.core

import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewClient : WebViewClient() {

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view?.loadUrl(url!!)
        return false
    }
}

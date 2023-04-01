package com.mfklcp.nota10net.presentation.home

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.DownloadListener
import android.webkit.URLUtil
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import com.mfklcp.nota10net.core.WebViewClient
import com.mfklcp.nota10net.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val webSettings: WebSettings = binding.mainWebViewNota10.settings
        webSettings.javaScriptEnabled = true
        setViews()
    }

    override fun onBackPressed() {
        binding.mainWebViewNota10.loadUrl(URL_HOME)
    }

    private fun setViews() {
        configureDownloadListener()
        setWebConfiguration()
        setHomeButtonAction()
        setExitButtonAction()
        setImageButtonAction()
    }

    private fun configureDownloadListener() {
        val downloadListener =
            DownloadListener { url, _, contentDisposition, mimetype, _ ->
                if (url != null) {
                    val request = DownloadManager.Request(Uri.parse(url))
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype))
                    request.setMimeType(mimetype)
                    request.setDescription("Downloading file...")
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype))
                    val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    downloadManager.enqueue(request)
                }
            }

        binding.mainWebViewNota10.setDownloadListener(downloadListener)
    }

    private fun setWebConfiguration() {
        with(binding.mainWebViewNota10) {
            webViewClient = WebViewClient()
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            clearCache(true)
            loadUrl(URL_HOME)
        }
    }

    private fun setImageButtonAction() {
        binding.titleHomeHeaderNota10App.setOnClickListener {
            binding.mainWebViewNota10.loadUrl(URL_HOME)
        }
    }

    private fun setHomeButtonAction() {
        binding.iconNota10.setOnClickListener {
            binding.mainWebViewNota10.loadUrl(URL_HOME)
        }
    }

    private fun setExitButtonAction() {
        binding.titleExitHeaderNota10App.setOnClickListener {
            binding.mainWebViewNota10.loadUrl(URL_SIGN_OUT)
        }
    }

    companion object {
        private const val URL_HOME = "https://nota10net.mikweb.com.br/central/painel/"
        private const val URL_SIGN_OUT = "https://nota10net.mikweb.com.br/central/signout"
    }
}

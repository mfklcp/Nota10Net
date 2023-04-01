package com.mfklcp.nota10net.core

import android.net.Uri
import android.os.Environment
import android.webkit.URLUtil
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.webkit.DownloadListener
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mfklcp.nota10net.databinding.ActivityMainBinding
import java.io.File

class CustomDownloadListener(
    private val context: Context,
    private val binding: ActivityMainBinding
) : DownloadListener {

    override fun onDownloadStart(
        url: String?,
        userAgent: String?,
        contentDisposition: String?,
        mimetype: String?,
        contentLength: Long
    ) {
        if (url != null && URLUtil.isValidUrl(url)) {
            val fileName = URLUtil.guessFileName(url, contentDisposition, mimetype)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissionCheck = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_CODE
                    )
                    return
                }
            }

            val directory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!directory.exists()) {
                directory.mkdirs()
            }

            val file = File(directory, fileName)
            if (file.exists()) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Download")
                builder.setMessage("Arquivo já existe! Você quer substituí-lo?")
                builder.setPositiveButton("Sim") { _, _ -> downloadFile(url, file) }
                builder.setNegativeButton("Não", null)
                builder.show()
            } else {
                downloadFile(url, file)
            }
        }
    }

    private fun downloadFile(url: String, file: File) {
        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as android.app.DownloadManager
        val request = android.app.DownloadManager.Request(Uri.parse(url))
        request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationUri(Uri.fromFile(file))
        downloadManager.enqueue(request)
    }

    companion object {
        private const val REQUEST_CODE = 100
    }
}

package com.example.transcribeapp.activities

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.transcribeapp.R
import com.google.android.material.snackbar.Snackbar

class WebViewActivity : AppCompatActivity() {
    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124
    private var webViewPreviousState = 0
    private val PAGE_STARTED = 0x1
    private val PAGE_REDIRECTED = 0x2
    private var rootView: CoordinatorLayout? = null
    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView = findViewById(R.id.webView1)
        rootView = findViewById<View>(R.id.root_view) as CoordinatorLayout

        if (Build.VERSION.SDK_INT >= 23) {
            askRuntimePermission()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }

        configureWebView()
        webView!!.loadUrl("https://sosflex.com/")
    }

    private fun configureWebView() {
        webView!!.setInitialScale(1)
        webView!!.settings.loadWithOverviewMode = true
        webView!!.settings.useWideViewPort = true
        webView!!.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView!!.isScrollbarFadingEnabled = false
        webView!!.settings.javaScriptCanOpenWindowsAutomatically = true
        webView!!.settings.builtInZoomControls = true
        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.setGeolocationEnabled(true)
        webView!!.settings.databaseEnabled = true
        webView!!.settings.domStorageEnabled = true
        webView!!.settings.setGeolocationDatabasePath(filesDir.path)

        // Enable mixed content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView!!.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // Enable debugging for better error logs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        webView!!.webViewClient = GeoWebViewClient()
        webView!!.webChromeClient = GeoWebChromeClient()
    }

    inner class GeoWebChromeClient : WebChromeClient() {
        override fun onGeolocationPermissionsShowPrompt(
            origin: String,
            callback: GeolocationPermissions.Callback
        ) {
            callback.invoke(origin, true, false)
        }
    }

    inner class GeoWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        var loadingDialog: Dialog? = null

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            webViewPreviousState = PAGE_STARTED
            if (loadingDialog == null || !loadingDialog!!.isShowing) {
                loadingDialog = ProgressDialog.show(
                    this@WebViewActivity, "",
                    "Loading Please Wait", true, true
                ) {
                    // do something
                }
                loadingDialog!!.setCancelable(false)
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView, request: WebResourceRequest,
            error: WebResourceError
        ) {
            handleError(error.description.toString())
            super.onReceivedError(view, request, error)
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun onReceivedHttpError(
            view: WebView,
            request: WebResourceRequest, errorResponse: WebResourceResponse
        ) {
            handleError(errorResponse.reasonPhrase)
            super.onReceivedHttpError(view, request, errorResponse)
        }

        override fun onPageFinished(view: WebView, url: String) {
            if (webViewPreviousState == PAGE_STARTED) {
                loadingDialog?.let {
                    if (it.isShowing) {
                        it.dismiss()
                    }
                    loadingDialog = null
                }
            }
        }

        private fun handleError(message: String) {
            if (isConnected) {
                val snackBar = Snackbar.make(rootView!!, "Error: $message", Snackbar.LENGTH_INDEFINITE)
                snackBar.setAction("Reload") { webView!!.loadUrl("javascript:window.location.reload( true )") }
                snackBar.show()
            } else {
                val snackBar = Snackbar.make(rootView!!, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                snackBar.setAction("Enable Data") {
                    startActivityForResult(Intent(Settings.ACTION_WIRELESS_SETTINGS), 0)
                    webView!!.loadUrl("javascript:window.location.reload( true )")
                    snackBar.dismiss()
                }
                snackBar.show()
            }
        }
    }

    val isConnected: Boolean
        get() {
            val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo?.isConnected == true
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                val perms: MutableMap<String, Int> = HashMap()
                perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
                for (i in permissions.indices) {
                    perms[permissions[i]] = grantResults[i]
                }
                if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "All Permissions GRANTED", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permissions DENIED. Exiting App.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun askRuntimePermission() {
        val permissionsNeeded: MutableList<String> = ArrayList()
        val permissionsList: MutableList<String> = ArrayList()
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionsNeeded.add("Show Location")
        }
        if (permissionsList.isNotEmpty()) {
            if (permissionsNeeded.isNotEmpty()) {
                var message = "App needs access to ${permissionsNeeded[0]}"
                for (i in 1 until permissionsNeeded.size) {
                    message += ", ${permissionsNeeded[i]}"
                }
                showMessageOKCancel(message) { _, _ ->
                    requestPermissions(
                        permissionsList.toTypedArray(),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
                    )
                }
                return
            }
            requestPermissions(
                permissionsList.toTypedArray(),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
        } else {
            Toast.makeText(this, "No new permissions required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun addPermission(permissionsList: MutableList<String>, permission: String): Boolean {
        return if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission)
            !shouldShowRequestPermissionRationale(permission)
        } else true
    }
}

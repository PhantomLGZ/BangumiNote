package com.phantom.banguminote.detail.web

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.viewModels
import com.phantom.banguminote.base.BaseActivity
import com.phantom.banguminote.base.http.pixiv.PixivWebViewNetworkHandler
import com.phantom.banguminote.base.http.pixiv.needBlock
import com.phantom.banguminote.base.requestinspectorwebview.RequestInspectorWebViewClient
import com.phantom.banguminote.base.requestinspectorwebview.WebViewRequest
import com.phantom.banguminote.databinding.ActivityWebViewBinding
import java.io.ByteArrayInputStream
import kotlin.Exception
import kotlin.Int
import kotlin.String
import kotlin.also
import kotlin.getValue


class RelatedWebViewActivity : BaseActivity<ActivityWebViewBinding>() {

    private val viewModel: WebViewModel by viewModels()

    override fun inflateViewBinding(): ActivityWebViewBinding =
        ActivityWebViewBinding.inflate(layoutInflater)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel.getIP()
        val url = intent.extras?.getString(KEY_URL) ?: ""
        binding.webView.also { wv ->
            wv.settings.also {
                it.javaScriptEnabled = true
                it.allowContentAccess = true
                it.domStorageEnabled = true
                it.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                // 允许javascript出错
                try {
                    val method = Class.forName("android.webkit.WebView")
                        .getMethod("setWebContentsDebuggingEnabled", Boolean::class.java)
                    method.isAccessible = true
                    method.invoke(null, true)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            wv.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                    println("TEST ${newProgress}")
                }
            }
            wv.webViewClient = object : RequestInspectorWebViewClient(wv) {
                @SuppressLint("WebViewClientOnReceivedSslError")
                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler,
                    error: SslError?
                ) {
                    handler.proceed() // Ignore SSL certificate errors
                }

                override fun shouldInterceptRequest(
                    view: WebView,
                    webViewRequest: WebViewRequest
                ): WebResourceResponse? {
                    if (webViewRequest.url.needBlock()) {
                        return WebResourceResponse(
                            "text/plain",
                            "utf-8",
                            ByteArrayInputStream("".toByteArray())
                        )
                    }

                    if (webViewRequest.url.contains("www.pixiv.net")
                        || webViewRequest.url.contains(".pximg.net")
                    ) {
                        if (webViewRequest.method == "GET") {
                            try {
                                return PixivWebViewNetworkHandler(webViewRequest)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else if (webViewRequest.method == "POST") {
                            try {
                                return PixivWebViewNetworkHandler(webViewRequest)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    return super.shouldInterceptRequest(view, webViewRequest)
                }
            }
            wv.loadUrl(url)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        const val KEY_URL = "KEY_URL"

        fun createBundle(url: String?): Bundle = Bundle().also {
            it.putString(KEY_URL, url)
        }
    }
}
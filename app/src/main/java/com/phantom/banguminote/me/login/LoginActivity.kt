package com.phantom.banguminote.me.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.activity.viewModels
import androidx.core.net.toUri
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseActivity
import com.phantom.banguminote.base.LoadingDialogFragment
import com.phantom.banguminote.base.http.pixiv.PixivWebViewNetworkHandler
import com.phantom.banguminote.base.requestinspectorwebview.RequestInspectorWebViewClient
import com.phantom.banguminote.base.requestinspectorwebview.WebViewRequest
import com.phantom.banguminote.base.setUserName
import com.phantom.banguminote.base.setUserToken
import com.phantom.banguminote.databinding.ActivityLoginBinding
import com.phantom.banguminote.me.login.data.AccessTokenReq
import com.phantom.banguminote.me.login.data.CLIENT_ID
import com.phantom.banguminote.me.login.data.REDIRECT_URI
import kotlin.random.Random


class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val randomState = Random.Default.nextInt()
    private val viewModel: LoginViewModel by viewModels()
    private val url =
        "https://bangumi.tv/oauth/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code&scope=&state=${randomState}"
    private val dialog = LoadingDialogFragment()

    override fun inflateViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("", "url = $url")
        viewModel.accessTokenRes.observe(this) {
            setUserToken(it.access_token)
            setUserName("")
            dialog.dismiss()
            finish()
        }
        binding.webView.also { wv ->
            wv.settings.also {
                it.javaScriptEnabled = true
                it.allowContentAccess = true
                it.domStorageEnabled = true
            }
            wv.webViewClient = object : RequestInspectorWebViewClient(wv) {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    if (request?.url?.toString()?.startsWith(REDIRECT_URI) == true) {
                        val code = request.url.getQueryParameter("code") ?: ""
                        val state = request.url.getQueryParameter("state")?.toInt()
                        if (state == randomState) {
                            viewModel.accessToken(AccessTokenReq(code = code, state = randomState))
                            dialog.show(supportFragmentManager, "")
                        } else {
                            showToast(getString(R.string.auth_error))
                        }
                        return true
                    } else if (
                        request?.url?.toString()?.contains("bangumi.tv/oauth/authorize") == true
                        && request.url?.getQueryParameter("redirect_uri") == null
                    ) {
                        view?.loadUrl(url)
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun shouldInterceptRequest(
                    view: WebView,
                    webViewRequest: WebViewRequest
                ): WebResourceResponse? {
                    if (!webViewRequest.url.startsWith(REDIRECT_URI)) {
                        if (webViewRequest.method == "GET") {
                            try {
                                return PixivWebViewNetworkHandler(webViewRequest)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else if (webViewRequest.method == "POST") {
                            try {
                                return PixivWebViewNetworkHandler(webViewRequest, view)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        val url = webViewRequest.url.toUri()
                        val code = url.getQueryParameter("code") ?: ""
                        val state = url.getQueryParameter("state")?.toInt()
                        if (state == randomState) {
                            viewModel.accessToken(AccessTokenReq(code = code, state = randomState))
                            dialog.show(supportFragmentManager, "")
                        } else {
                            showToast(getString(R.string.auth_error))
                        }
                    }

                    return super.shouldInterceptRequest(view, webViewRequest)
                }
            }
            wv.loadUrl(url)
        }
    }
}
package com.phantom.banguminote.me.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseActivity
import com.phantom.banguminote.base.LoadingDialogFragment
import com.phantom.banguminote.base.setUserToken
import com.phantom.banguminote.databinding.ActivityLoginBinding
import com.phantom.banguminote.me.login.data.AccessTokenReq
import kotlin.random.Random


class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val randomState = Random.Default.nextInt()
    private val viewModel: LoginViewModel by viewModels()
    private val url =
        "https://bgm.tv/oauth/authorize?client_id=bgm2897659da10d90152&redirect_uri=https%3A%2F%2Fphantom&response_type=code&scope=&state=${randomState}"
    private val dialog = LoadingDialogFragment()

    override fun inflateViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.accessTokenRes.observe(this) {
            setUserToken(it.access_token)
            dialog.dismiss()
            finish()
        }
        binding.webView.also { wv ->
            wv.settings.also {
                it.javaScriptEnabled = true
                it.allowContentAccess = true
                it.domStorageEnabled = true
            }
            wv.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    if (request?.url?.toString()?.startsWith("https://phantom") == true) {
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
                        request?.url?.toString()?.contains("bgm.tv/oauth/authorize") == true
                        && request.url?.getQueryParameter("redirect_uri") == null
                    ) {
                        view?.loadUrl(url)
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }
            wv.loadUrl(url)
        }
    }
}
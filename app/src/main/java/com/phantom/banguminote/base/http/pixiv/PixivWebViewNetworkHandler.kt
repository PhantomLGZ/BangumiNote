package com.phantom.banguminote.base.http.pixiv

import android.webkit.WebResourceResponse
import com.phantom.banguminote.base.http.WebkitCookieManagerProxy
import com.phantom.banguminote.base.requestinspectorwebview.WebViewRequest
import okhttp3.ConnectionSpec
import okhttp3.Headers.Companion.toHeaders
import okhttp3.JavaNetCookieJar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.tls.OkHostnameVerifier
import java.net.CookieManager
import java.nio.charset.Charset
import java.security.KeyStore
import java.util.Arrays
import java.util.Collections
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


object PixivWebViewNetworkHandler {
    private val cookieManagerProxy = getCookieManager()
    private val client = makeClient()

    operator fun invoke(webResourceRequest: WebViewRequest?): WebResourceResponse? {

        var url = webResourceRequest?.url.toString()
        if (url == "https://www.pixiv.net/ajax/login?lang=zh") {
            url = "https://accounts.pixiv.net/ajax/login?lang=zh"
        }
        val headers = webResourceRequest?.headers?.toHeaders()
        val newRequest = headers?.let { h ->
            Request.Builder()
                .url(url)
                .also {
                    if (webResourceRequest.method == "POST") {
                        it.post(webResourceRequest.body.toRequestBody("application/x-www-form-urlencoded".toMediaType()))
                    }
                }
                .headers(h)
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .build()
        }

        val response = newRequest
            ?.let { client.newCall(newRequest).execute() }
            ?.let {
                WebResourceResponse(
                    it.body?.contentType()?.let { "${it.type}/${it.subtype}" },
                    it.body?.contentType()?.charset(Charset.defaultCharset())?.name(),
                    it.code,
                    "OK",
                    it.headers.toMap(),
                    it.body?.byteStream()
                )
            }

        return response
    }

    private fun makeClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .sslSocketFactory(PixivSSLSocketFactory(), getTrustManager())
            .hostnameVerifier { hostname, session ->
                if (hostname.contains("pixiv") || hostname.contains("pximg")) {
                    true
                } else {
                    OkHostnameVerifier.verify(hostname, session)
                }
            }
            .cookieJar(JavaNetCookieJar(cookieManagerProxy))
            .connectionSpecs(Collections.singletonList(ConnectionSpec.COMPATIBLE_TLS))
            .dns(PixivDns)
            .build()
    }

    private fun getTrustManager(): X509TrustManager {
        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + Arrays.toString(trustManagers)
        }
        return trustManagers[0] as X509TrustManager
    }

    private fun getCookieManager(): CookieManager {
        return WebkitCookieManagerProxy()
    }

}
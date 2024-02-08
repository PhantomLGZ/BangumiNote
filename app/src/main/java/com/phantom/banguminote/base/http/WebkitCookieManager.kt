package com.phantom.banguminote.base.http

import java.io.IOException
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.CookieStore
import java.net.URI

class WebkitCookieManagerProxy internal constructor(
    store: CookieStore?,
    cookiePolicy: CookiePolicy?
) : CookieManager(null, cookiePolicy) {

    private val webkitCookieManager = android.webkit.CookieManager.getInstance()

    constructor() : this(null, null)

    @Throws(IOException::class)
    override fun put(uri: URI?, responseHeaders: Map<String?, List<String?>>?) {
        // make sure our args are valid
        if (uri == null || responseHeaders == null) return

        // save our url once
        val url = uri.toString()

        // go over the headers
        for (headerKey in responseHeaders.keys) {
            // ignore headers which aren't cookie related
            if (headerKey == null || !(headerKey.equals(
                    "Set-Cookie2",
                    ignoreCase = true
                ) || headerKey.equals("Set-Cookie", ignoreCase = true))
            ) continue

            // process each of the headers
            for (headerValue in responseHeaders[headerKey]!!) {
                webkitCookieManager.setCookie(url, headerValue)
            }
        }
    }

    @Throws(IOException::class)
    override operator fun get(
        uri: URI?,
        requestHeaders: Map<String?, List<String?>?>?
    ): Map<String, List<String>> {
        // make sure our args are valid
        require(!(uri == null || requestHeaders == null)) { "Argument is null" }

        // save our url once
        val url = uri.toString()

        // prepare our response
        val res: MutableMap<String, List<String>> = HashMap()

        // get the cookie
        val cookie = webkitCookieManager.getCookie(url)

        // return it
        if (cookie != null) res["Cookie"] = listOf(cookie)
        return res
    }

    override fun getCookieStore(): CookieStore {
        throw UnsupportedOperationException()
    }
}
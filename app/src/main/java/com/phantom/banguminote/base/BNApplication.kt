package com.phantom.banguminote.base

import android.app.Application
import android.webkit.CookieManager
import com.phantom.banguminote.base.http.WebkitCookieManagerProxy
import java.net.CookieHandler
import java.net.CookiePolicy


class BNApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CookieManager.getInstance().setAcceptCookie(true)

        val coreCookieManager = WebkitCookieManagerProxy(null, CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(coreCookieManager)
    }
}
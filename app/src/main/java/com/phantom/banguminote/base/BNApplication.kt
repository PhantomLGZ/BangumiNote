package com.phantom.banguminote.base

import android.app.Application
import android.webkit.CookieManager
import com.phantom.banguminote.base.http.WebkitCookieManagerProxy
import org.conscrypt.Conscrypt
import java.net.CookieHandler
import java.net.CookiePolicy
import java.security.Security


class BNApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CookieManager.getInstance().setAcceptCookie(true)

        val coreCookieManager = WebkitCookieManagerProxy(null, CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(coreCookieManager)
        Security.insertProviderAt(Conscrypt.newProvider(), 1)
    }
}
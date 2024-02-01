package com.phantom.banguminote.me.login

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.base.http.RetrofitHelper
import com.phantom.banguminote.me.login.data.AccessTokenReq
import com.phantom.banguminote.me.login.data.AccessTokenRes
import com.phantom.banguminote.me.login.data.TokenStatusRes

class LoginViewModel : BaseViewModel() {

    private val authorizationHttpServer =
        RetrofitHelper.getAuthorizationHttpServer(IAuthorizationHttpServer::class.java)

    val accessTokenRes = MutableLiveData<AccessTokenRes>()
    val tokenStatusRes = MutableLiveData<TokenStatusRes>()

    fun accessToken(req: AccessTokenReq) {
        requestWithCoroutine(req, accessTokenRes) { authorizationHttpServer.accessToken(it) }
    }

    fun tokenStatus() {
        requestWithCoroutine("", tokenStatusRes) { authorizationHttpServer.tokenStatus() }
    }

}
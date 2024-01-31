package com.phantom.banguminote.me.login

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.base.http.RetrofitHelper
import com.phantom.banguminote.me.login.data.AccessTokenReq
import com.phantom.banguminote.me.login.data.AccessTokenRes

class LoginViewModel : BaseViewModel() {

    val accessTokenRes = MutableLiveData<AccessTokenRes>()

    private val authorizationHttpServer =
        RetrofitHelper.getAuthorizationHttpServer(IAuthorizationHttpServer::class.java)

    fun accessToken(req: AccessTokenReq) {
        requestWithCoroutine(req, accessTokenRes) { authorizationHttpServer.accessToken(it) }
    }

}
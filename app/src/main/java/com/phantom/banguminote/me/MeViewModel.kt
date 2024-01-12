package com.phantom.banguminote.me

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.authorization.IAuthorizationHttpServer
import com.phantom.banguminote.authorization.data.AuthorizationReq
import com.phantom.banguminote.authorization.data.AuthorizationRes
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.base.http.RetrofitHelper.getAuthorizationHttpServer
import com.phantom.banguminote.base.http.toMap

class MeViewModel : BaseViewModel() {

    private val authorizationHttpServer = getAuthorizationHttpServer(IAuthorizationHttpServer::class.java)

    val authorizeRes = MutableLiveData<AuthorizationRes>()

    fun authorize(req: AuthorizationReq) {
        requestWithCoroutine(req, authorizeRes) { authorizationHttpServer.authorize(it.toMap()) }
    }

}
package com.phantom.banguminote.me

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.IBangumiHttpServer
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.base.http.RetrofitHelper
import com.phantom.banguminote.me.data.UserData

class MeViewModel : BaseViewModel() {

    private val iHttpServer = RetrofitHelper.getHttpServer(IBangumiHttpServer::class.java)

    val userRes = MutableLiveData<UserData>()

    fun me() {
        requestWithCoroutine("", userRes) { iHttpServer.me() }
    }

}
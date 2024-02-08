package com.phantom.banguminote.detail.web

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.base.http.RetrofitHelper

class WebViewModel : BaseViewModel() {

    private val iHttpServer = RetrofitHelper.getDnsServer(IDnsServer::class.java)

    val idRes = MutableLiveData<DNSData>()

    fun getIP(host: String = "www.pixiv.net") {
        requestWithCoroutine(host, idRes) { iHttpServer.getIP(name = it) }
    }

}
package com.phantom.banguminote.subject

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.IBangumiHttpServer
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.base.http.RetrofitHelper

class SubjectViewModel: BaseViewModel() {

    private val iBangumiHttpServer = RetrofitHelper.getHttpServer(IBangumiHttpServer::class.java)

    val subjectRes = MutableLiveData<SubjectData>()

    fun subject(id: Int) {
        requestWithCoroutine(id, subjectRes) { iBangumiHttpServer.subject(it) }
    }

}
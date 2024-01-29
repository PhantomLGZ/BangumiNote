package com.phantom.banguminote.search

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.IBangumiHttpServer
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.data.PageResData
import com.phantom.banguminote.base.http.RetrofitHelper
import com.phantom.banguminote.data.PageReqData

class SearchViewModel : BaseViewModel() {

    private val iHttpServer = RetrofitHelper.getHttpServer(IBangumiHttpServer::class.java)

    val searchReq = MutableLiveData<SearchReq>()
    val searchRes = MutableLiveData<PageResData<SearchRes>>()

    fun search(searchReq: PageReqData<SearchReq>) {
        requestWithCoroutine(searchReq, searchRes) {
            iHttpServer.search(it.limit, it.offset, it.req)
        }
    }

}
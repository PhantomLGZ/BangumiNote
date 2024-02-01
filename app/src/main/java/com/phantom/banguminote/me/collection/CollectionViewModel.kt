package com.phantom.banguminote.me.collection

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.IBangumiHttpServer
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.base.http.RetrofitHelper
import com.phantom.banguminote.data.PageReqData
import com.phantom.banguminote.data.PageResData

class CollectionViewModel : BaseViewModel() {

    private val iHttpServer = RetrofitHelper.getHttpServer(IBangumiHttpServer::class.java)

    val collectionRes = MutableLiveData<PageResData<CollectionItemRes>>()

    fun getCollections(req: PageReqData<CollectionsReq>) {
        requestWithCoroutine(req, collectionRes) {
            iHttpServer.getCollections(
                it.req.username, it.req.subjectType, it.req.collectionType, it.limit, it.offset
            )
        }
    }

}
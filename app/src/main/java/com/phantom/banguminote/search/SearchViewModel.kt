package com.phantom.banguminote.search

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.IBangumiHttpServer
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.data.PageResData
import com.phantom.banguminote.base.http.RetrofitHelper
import com.phantom.banguminote.data.PageReqData
import com.phantom.banguminote.detail.character.data.CharacterData
import com.phantom.banguminote.detail.person.data.PersonData
import com.phantom.banguminote.search.data.SearchCharacterReq
import com.phantom.banguminote.search.data.SearchPersonReq
import com.phantom.banguminote.search.data.SearchSubjectReq
import com.phantom.banguminote.search.data.SearchSubjectRes

class SearchViewModel : BaseViewModel() {

    private val iHttpServer = RetrofitHelper.getHttpServer(IBangumiHttpServer::class.java)

    var searchSubjectReq = SearchSubjectReq()
    val searchSubjectRes = MutableLiveData<PageResData<SearchSubjectRes>>()

    var searchCharacterReq = SearchCharacterReq()
    val searchCharacterRes = MutableLiveData<PageResData<CharacterData>>()

    var searchPersonReq = SearchPersonReq()
    val searchPersonRes = MutableLiveData<PageResData<PersonData>>()

    fun searchSubject(req: PageReqData<SearchSubjectReq>) {
        requestWithCoroutine(req, searchSubjectRes) {
            iHttpServer.searchSubject(it.limit, it.offset, it.req)
        }
    }

    fun searchCharacter(req: PageReqData<SearchCharacterReq>) {
        requestWithCoroutine(req, searchCharacterRes) {
            iHttpServer.searchCharacter(it.limit, it.offset, it.req)
        }
    }

    fun searchPerson(req: PageReqData<SearchPersonReq>) {
        requestWithCoroutine(req, searchPersonRes) {
            iHttpServer.searchPerson(it.limit, it.offset, it.req)
        }
    }

}
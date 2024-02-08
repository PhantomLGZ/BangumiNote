package com.phantom.banguminote.detail.person

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.IBangumiHttpServer
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.base.http.RetrofitHelper
import com.phantom.banguminote.detail.person.data.PersonCharacterData
import com.phantom.banguminote.detail.person.data.PersonData
import com.phantom.banguminote.detail.person.data.PersonRelatedData

class PersonViewModel : BaseViewModel() {

    private val iHttpServer = RetrofitHelper.getHttpServer(IBangumiHttpServer::class.java)

    val id = MutableLiveData<Int>()
    val personRes = MutableLiveData<PersonData>()
    val personRelatedRes = MutableLiveData<List<PersonRelatedData>>()
    val personCharacterRes = MutableLiveData<List<PersonCharacterData>>()

    fun person(id: Int) {
        requestWithCoroutine(id, personRes) { iHttpServer.person(it) }
    }

    fun personRelated(id: Int) {
        requestWithCoroutine(id, personRelatedRes) { iHttpServer.personRelated(it) }
    }

    fun personCharacter(id: Int) {
        requestWithCoroutine(id, personCharacterRes) { iHttpServer.personCharacter(it) }
    }

}
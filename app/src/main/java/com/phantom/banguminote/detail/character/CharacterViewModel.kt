package com.phantom.banguminote.detail.character

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.IBangumiHttpServer
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.base.http.RetrofitHelper
import com.phantom.banguminote.detail.character.data.CharacterData
import com.phantom.banguminote.detail.character.data.CharacterPersonData
import com.phantom.banguminote.detail.character.data.CharacterRelatedData

class CharacterViewModel : BaseViewModel() {

    private val iHttpServer = RetrofitHelper.getHttpServer(IBangumiHttpServer::class.java)

    val characterRes = MutableLiveData<CharacterData>()
    val characterRelatedRes = MutableLiveData<List<CharacterRelatedData>>()
    val characterPersonRes = MutableLiveData<List<CharacterPersonData>>()

    fun character(id: Int) {
        requestWithCoroutine(id, characterRes) { iHttpServer.character(it) }
    }

    fun characterRelated(id: Int) {
        requestWithCoroutine(id, characterRelatedRes) { iHttpServer.characterRelatedSubjects(it) }
    }

    fun characterPerson(id: Int) {
        requestWithCoroutine(id, characterPersonRes) { iHttpServer.characterPersons(it) }
    }

}
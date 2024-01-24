package com.phantom.banguminote.subject

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.IBangumiHttpServer
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.base.http.RetrofitHelper
import com.phantom.banguminote.data.CharacterData
import com.phantom.banguminote.data.PersonData
import com.phantom.banguminote.data.RelatedSubjectData

class SubjectViewModel : BaseViewModel() {

    private val iBangumiHttpServer = RetrofitHelper.getHttpServer(IBangumiHttpServer::class.java)

    val subjectRes = MutableLiveData<SubjectData>()
    val subjectPersonRes = MutableLiveData<List<PersonData>>()
    val subjectCharacterRes = MutableLiveData<List<CharacterData>>()
    val subjectRelatedSubjectRes = MutableLiveData<List<RelatedSubjectData>>()

    fun subject(id: Int) {
        requestWithCoroutine(id, subjectRes) { iBangumiHttpServer.subject(it) }
    }

    fun subjectPersons(id: Int) {
        requestWithCoroutine(id, subjectPersonRes) { iBangumiHttpServer.subjectPersons(it) }
    }

    fun subjectCharacter(id: Int) {
        requestWithCoroutine(id, subjectCharacterRes) { iBangumiHttpServer.subjectCharacters(it) }
    }

    fun subjectRelatedSubjects(id: Int) {
        requestWithCoroutine(id, subjectRelatedSubjectRes) { iBangumiHttpServer.subjectRelatedSubjects(it) }
    }

}
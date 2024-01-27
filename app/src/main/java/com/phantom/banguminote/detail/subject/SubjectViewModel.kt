package com.phantom.banguminote.detail.subject

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.IBangumiHttpServer
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.base.http.RetrofitHelper
import com.phantom.banguminote.detail.subject.data.RelatedSubjectData
import com.phantom.banguminote.detail.subject.data.SubjectCharacterData
import com.phantom.banguminote.detail.subject.data.SubjectData
import com.phantom.banguminote.detail.subject.data.SubjectPersonData

class SubjectViewModel : BaseViewModel() {

    private val iHttpServer = RetrofitHelper.getHttpServer(IBangumiHttpServer::class.java)

    val subjectRes = MutableLiveData<SubjectData>()
    val subjectPersonRes = MutableLiveData<List<SubjectPersonData>>()
    val subjectCharacterRes = MutableLiveData<List<SubjectCharacterData>>()
    val subjectRelatedSubjectRes = MutableLiveData<List<RelatedSubjectData>>()

    fun subject(id: Int) {
        requestWithCoroutine(id, subjectRes) { iHttpServer.subject(it) }
    }

    fun subjectPersons(id: Int) {
        requestWithCoroutine(id, subjectPersonRes) { iHttpServer.subjectPersons(it) }
    }

    fun subjectCharacter(id: Int) {
        requestWithCoroutine(id, subjectCharacterRes) { iHttpServer.subjectCharacters(it) }
    }

    fun subjectRelatedSubjects(id: Int) {
        requestWithCoroutine(id, subjectRelatedSubjectRes) { iHttpServer.subjectRelatedSubjects(it) }
    }

}
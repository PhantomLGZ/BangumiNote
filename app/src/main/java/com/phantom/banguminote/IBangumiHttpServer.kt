package com.phantom.banguminote

import com.phantom.banguminote.base.http.IHttpServer
import com.phantom.banguminote.data.CharacterData
import com.phantom.banguminote.data.PersonData
import com.phantom.banguminote.data.RelatedSubjectData
import com.phantom.banguminote.front.calendar.CalendarRes
import com.phantom.banguminote.subject.SubjectData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IBangumiHttpServer : IHttpServer {

    @GET("calendar")
    suspend fun calendar(): Response<List<CalendarRes>>

    @GET("v0/subjects/{subject_id}")
    suspend fun subject(@Path("subject_id") id: Int): Response<SubjectData>

    @GET("v0/subjects/{subject_id}/persons")
    suspend fun subjectPersons(@Path("subject_id") id: Int): Response<List<PersonData>>

    @GET("v0/subjects/{subject_id}/characters")
    suspend fun subjectCharacters(@Path("subject_id") id: Int): Response<List<CharacterData>>

    @GET("v0/subjects/{subject_id}/subjects")
    suspend fun subjectRelatedSubjects(@Path("subject_id") id: Int): Response<List<RelatedSubjectData>>

}
package com.phantom.banguminote

import com.phantom.banguminote.base.http.IHttpServer
import com.phantom.banguminote.data.PageResData
import com.phantom.banguminote.detail.character.data.CharacterData
import com.phantom.banguminote.detail.character.data.CharacterPersonData
import com.phantom.banguminote.detail.character.data.CharacterRelatedData
import com.phantom.banguminote.detail.person.data.PersonCharacterData
import com.phantom.banguminote.detail.person.data.PersonData
import com.phantom.banguminote.detail.person.data.PersonRelatedData
import com.phantom.banguminote.detail.subject.data.SubjectCharacterData
import com.phantom.banguminote.detail.subject.data.SubjectPersonData
import com.phantom.banguminote.detail.subject.data.RelatedSubjectData
import com.phantom.banguminote.calendar.CalendarRes
import com.phantom.banguminote.detail.subject.collection.ModifyCollectionReq
import com.phantom.banguminote.detail.subject.data.SubjectData
import com.phantom.banguminote.detail.subject.episode.EpisodeData
import com.phantom.banguminote.me.collection.CollectionItemRes
import com.phantom.banguminote.me.data.UserData
import com.phantom.banguminote.search.SearchReq
import com.phantom.banguminote.search.SearchRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IBangumiHttpServer : IHttpServer {

    @GET("calendar")
    suspend fun calendar(): Response<List<CalendarRes>>

    @POST("v0/search/subjects")
    suspend fun search(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Body req: SearchReq
    ): Response<PageResData<SearchRes>>

    @GET("v0/subjects/{subject_id}")
    suspend fun subject(@Path("subject_id") id: Int): Response<SubjectData>

    @GET("v0/subjects/{subject_id}/persons")
    suspend fun subjectPersons(@Path("subject_id") id: Int): Response<List<SubjectPersonData>>

    @GET("v0/subjects/{subject_id}/characters")
    suspend fun subjectCharacters(@Path("subject_id") id: Int): Response<List<SubjectCharacterData>>

    @GET("v0/subjects/{subject_id}/subjects")
    suspend fun subjectRelatedSubjects(@Path("subject_id") id: Int): Response<List<RelatedSubjectData>>

    @GET("v0/episodes")
    suspend fun episodes(
        @Query("subject_id") subjectId: Int,
        @Query("type") type: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response<PageResData<EpisodeData>>

    @GET("v0/characters/{character_id}")
    suspend fun character(@Path("character_id") id: Int): Response<CharacterData>

    @GET("v0/characters/{character_id}/subjects")
    suspend fun characterRelatedSubjects(@Path("character_id") id: Int): Response<List<CharacterRelatedData>>

    @GET("v0/characters/{character_id}/persons")
    suspend fun characterPersons(@Path("character_id") id: Int): Response<List<CharacterPersonData>>

    @GET("v0/persons/{person_id}")
    suspend fun person(@Path("person_id") id: Int): Response<PersonData>

    @GET("v0/persons/{person_id}/subjects")
    suspend fun personRelated(@Path("person_id") id: Int): Response<List<PersonRelatedData>>

    @GET("v0/persons/{person_id}/characters")
    suspend fun personCharacter(@Path("person_id") id: Int): Response<List<PersonCharacterData>>

    @GET("v0/me")
    suspend fun me(): Response<UserData>

    @GET("v0/users/{username}/collections")
    suspend fun getCollections(
        @Path("username") username: String,
        @Query("subject_type") subjectType: Int,
        @Query("type") collectionType: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response<PageResData<CollectionItemRes>>

    @GET("v0/users/{username}/collections/{subject_id}")
    suspend fun getCollectionInfo(
        @Path("username") username: String,
        @Path("subject_id") subjectId: Int,
    ): Response<CollectionItemRes>

    @POST("v0/users/-/collections/{subject_id}")
    suspend fun modifyCollection(
        @Path("subject_id") subjectId: Int,
        @Body req: ModifyCollectionReq
    ): Response<Unit>

}
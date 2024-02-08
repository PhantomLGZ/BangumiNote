package com.phantom.banguminote.detail.web

import com.phantom.banguminote.base.http.IHttpServer
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IDnsServer : IHttpServer {

    @GET("dns-query")
    suspend fun getIP(
        @Query("name") name: String = "www.pixiv.net",
        @Query("type") type: String = "A",
        @Query("do") dns_do: Boolean = false,
        @Query("cd") dns_cd: Boolean = false,
    ): Response<DNSData>

}
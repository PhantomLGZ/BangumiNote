package com.phantom.banguminote.me.login

import com.phantom.banguminote.me.login.data.AccessTokenReq
import com.phantom.banguminote.me.login.data.AccessTokenRes
import com.phantom.banguminote.base.http.IHttpServer
import com.phantom.banguminote.me.login.data.TokenStatusRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface IAuthorizationHttpServer : IHttpServer {

    @POST("oauth/access_token")
    suspend fun accessToken(@Body req: AccessTokenReq): Response<AccessTokenRes>

    @GET("oauth/token_status")
    suspend fun tokenStatus(): Response<TokenStatusRes>

}
package com.phantom.banguminote.authorization

import com.phantom.banguminote.authorization.data.AccessTokenReq
import com.phantom.banguminote.authorization.data.AccessTokenRes
import com.phantom.banguminote.authorization.data.AuthorizationRes
import com.phantom.banguminote.base.http.IHttpServer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap


interface IAuthorizationHttpServer: IHttpServer {

    @GET("oauth/authorize")
    suspend fun authorize(@QueryMap query: Map<String, String>): Response<AuthorizationRes>

    @POST("oauth/access_token")
    suspend fun accessToken(@Body req: AccessTokenReq): Response<AccessTokenRes>

}
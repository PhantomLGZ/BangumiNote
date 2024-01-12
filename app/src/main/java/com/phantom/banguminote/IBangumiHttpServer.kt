package com.phantom.banguminote

import com.phantom.banguminote.base.http.IHttpServer
import com.phantom.banguminote.front.calendar.CalendarRes
import retrofit2.Response
import retrofit2.http.GET

interface IBangumiHttpServer: IHttpServer {


    @GET("calendar")
    suspend fun calendar() : Response<List<CalendarRes>>


}
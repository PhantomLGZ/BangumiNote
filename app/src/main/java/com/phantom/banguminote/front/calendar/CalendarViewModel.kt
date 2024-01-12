package com.phantom.banguminote.front.calendar

import androidx.lifecycle.MutableLiveData
import com.phantom.banguminote.IBangumiHttpServer
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.base.http.RetrofitHelper

class CalendarViewModel: BaseViewModel() {

    private val iBangumiHttpServer = RetrofitHelper.getHttpServer(IBangumiHttpServer::class.java)

    val calendarRes = MutableLiveData<List<CalendarRes>>()

    fun calendar() {
        requestWithCoroutine("", calendarRes) { iBangumiHttpServer.calendar() }
    }

}
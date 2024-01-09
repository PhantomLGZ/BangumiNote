package com.phantom.banguminote.base.http

import com.phantom.banguminote.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    private val retrofit: Retrofit.Builder =
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().also {
                        it.level = if (BuildConfig.DEBUG) {
                            HttpLoggingInterceptor.Level.BODY
                        } else {
                            HttpLoggingInterceptor.Level.NONE
                        }
                    })
                    .build()
            )
            .baseUrl("http://192.168.50.140:8080/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

    fun getRetrofit(): Retrofit.Builder = retrofit

    fun setBaseUrl(baseUrl: String) {
        retrofit.baseUrl(baseUrl)
    }

    fun <T : IHttpServer> getHttpServer(server: Class<T>): T {
        return retrofit.build().create(server)
    }

}
package com.phantom.banguminote.base.http

import com.phantom.banguminote.BuildConfig
import com.phantom.banguminote.base.unicodeToString
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {

    private val retrofit: Retrofit.Builder =
        retrofitBuild("https://api.bgm.tv/")

    private val authorizationRetrofit: Retrofit.Builder =
        retrofitBuild("https://bgm.tv/")

    private fun retrofitBuild(url: String): Retrofit.Builder =
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addNetworkInterceptor(Interceptor {
                        val requestBuilder = it.request().newBuilder()
                        requestBuilder.header("Content-Type", "application/json; charset=utf-8")
                        it.proceed(requestBuilder.build())
                    })
                    .addInterceptor(HttpLoggingInterceptor {
                        Platform.get().log(it.unicodeToString())
                    }.also {
                        it.level = if (BuildConfig.DEBUG) {
                            HttpLoggingInterceptor.Level.BODY
                        } else {
                            HttpLoggingInterceptor.Level.NONE
                        }
                    })
                    .build()
            )
            .baseUrl(url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

    fun getRetrofit(): Retrofit.Builder = retrofit

    fun getAuthorizationRetrofit(): Retrofit.Builder = authorizationRetrofit

    fun <T : IHttpServer> getHttpServer(server: Class<T>): T {
        return retrofit.build().create(server)
    }

    fun <T : IHttpServer> getAuthorizationHttpServer(server: Class<T>): T {
        return authorizationRetrofit.build().create(server)
    }

}
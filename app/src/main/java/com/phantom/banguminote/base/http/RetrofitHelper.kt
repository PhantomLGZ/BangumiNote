package com.phantom.banguminote.base.http

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.phantom.banguminote.BuildConfig
import com.phantom.banguminote.base.unicodeToString
import com.phantom.banguminote.data.HttpErrorData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    val errorGson: Gson = GsonBuilder()
        .registerTypeAdapter(
            HttpErrorData::class.java,
            JsonDeserializer { json, typeOfT, context ->
                try {
                    return@JsonDeserializer json.asJsonObject.let {
                        HttpErrorData(
                            title = it.get("title").asString,
                            details = it.get("details").asJsonObject.toString(),
                            description = it.get("description").asString
                        )
                    }
                } catch (e: Exception) {
                    return@JsonDeserializer null
                }
            }
        )
        .create()

    private val retrofit: Retrofit.Builder = retrofitBuild()

    private var accessToken = ""

    fun setAccessToken(token: String) {
        accessToken = token
    }

    private fun retrofitBuild(): Retrofit.Builder =
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addNetworkInterceptor(Interceptor {
                        val requestBuilder = it.request().newBuilder()
                        requestBuilder
                            .header("Content-Type", "application/json; charset=utf-8")
                            .header(
                                "User-Agent",
                                "Phantom/BangumiNote/${BuildConfig.VERSION_NAME} (Android) (https://github.com/PhantomLGZ/BangumiNote)"
                            )
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
                    .addInterceptor(Interceptor {
                        return@Interceptor it.proceed(
                            if (accessToken.isNotBlank()) {
                                it.request().newBuilder()
                                    .header("Authorization", "Bearer $accessToken")
                                    .build()
                            } else {
                                it.request()
                            }
                        )
                    })
                    .build()
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

    fun getRetrofit(): Retrofit.Builder = retrofit

    fun <T : IHttpServer> getHttpServer(server: Class<T>): T {
        return retrofit
            .baseUrl("https://api.bgm.tv/")
            .build()
            .create(server)
    }

    fun <T : IHttpServer> getAuthorizationHttpServer(server: Class<T>): T {
        return retrofit
            .baseUrl("https://bgm.tv/")
            .build()
            .create(server)
    }

}
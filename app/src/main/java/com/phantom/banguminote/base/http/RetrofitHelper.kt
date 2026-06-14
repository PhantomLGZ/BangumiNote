package com.phantom.banguminote.base.http

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.phantom.banguminote.BuildConfig
import com.phantom.banguminote.base.http.pixiv.PixivDns
import com.phantom.banguminote.base.http.pixiv.PixivSSLSocketFactory
import com.phantom.banguminote.base.unicodeToString
import com.phantom.banguminote.data.HttpErrorData
import okhttp3.ConnectionSpec
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.internal.platform.Platform
import okhttp3.internal.tls.OkHostnameVerifier
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.security.KeyStore
import java.util.Collections
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


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
    private val authorizationRetrofit: Retrofit.Builder = authorizationRetrofitBuild()
    private val dnsRetrofit: Retrofit.Builder = dnsRetrofitBuild()

    private var accessToken = ""

    fun setAccessToken(token: String) {
        accessToken = token
    }

    private fun dnsRetrofitBuild(): Retrofit.Builder =
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addNetworkInterceptor(Interceptor {
                        val requestBuilder = it.request().newBuilder()
                        requestBuilder
                            .header("Accept", "application/dns-json")
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
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://1.0.0.1/")

    private val bootstrapClient = OkHttpClient.Builder().build()
    private val dnsOverHttps = DnsOverHttps.Builder()
        .client(bootstrapClient)
        .url("https://cloudflare-dns.com/dns-query".toHttpUrl())
        .bootstrapDnsHosts(
            InetAddress.getByName("1.1.1.1"),
            InetAddress.getByName("1.0.0.1")
        )
        .build()


    private fun retrofitBuild(): Retrofit.Builder =
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .connectionSpecs(Collections.singletonList(ConnectionSpec.MODERN_TLS))
                    .addNetworkInterceptor(Interceptor {
                        val requestBuilder = it.request().newBuilder()
                        requestBuilder
                            .header("Content-Type", "application/json; charset=UTF-8")
                            .header(
                                "User-Agent",
                                "Phantom/BangumiNote/${BuildConfig.VERSION_NAME} (Android) (https://github.com/PhantomLGZ/BangumiNote)"
                            )
                        val res = it.proceed(requestBuilder.build())
                        res
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

    private fun authorizationRetrofitBuild(): Retrofit.Builder =
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .connectionSpecs(Collections.singletonList(ConnectionSpec.MODERN_TLS))
                    .dns(PixivDns)
                    .sslSocketFactory(PixivSSLSocketFactory(), getTrustManager())
                    .hostnameVerifier { hostname, session ->
                        if (hostname.contains("bgm.tv")
                            || hostname.contains("bangumi.tv")
                        ) {
                            true
                        } else {
                            OkHostnameVerifier.verify(hostname, session)
                        }
                    }
                    .addNetworkInterceptor(Interceptor {
                        val requestBuilder = it.request().newBuilder()
                        requestBuilder
                            .header("Content-Type", "application/json; charset=UTF-8")
                            .header(
                                "User-Agent",
                                "Phantom/BangumiNote/${BuildConfig.VERSION_NAME} (Android) (https://github.com/PhantomLGZ/BangumiNote)"
                            )
                        val res = it.proceed(requestBuilder.build())
                        res
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

    private fun getTrustManager(): X509TrustManager {
        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + trustManagers.contentToString()
        }
        return trustManagers[0] as X509TrustManager
    }

    fun <T : IHttpServer> getDnsServer(server: Class<T>): T {
        return dnsRetrofit.build().create(server)
    }

    fun <T : IHttpServer> getHttpServer(server: Class<T>): T {
        return retrofit
            .baseUrl("https://api.bgmapi.com/")
            .build()
            .create(server)
    }

    fun <T : IHttpServer> getAuthorizationHttpServer(server: Class<T>): T {
        return authorizationRetrofit
            .baseUrl("https://bangumi.tv/")
            .build()
            .create(server)
    }

}
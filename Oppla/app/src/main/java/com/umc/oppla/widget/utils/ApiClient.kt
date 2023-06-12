package com.umc.oppla.widget.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.umc.oppla.ApplicationClass
import com.umc.oppla.data.local.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL =
        "http://oppla-env.eba-iuykwqzn.ap-northeast-2.elasticbeanstalk.com/" // API 의 base_url
    private val gson: Gson = GsonBuilder().setLenient().create()
    private var jwtToken: String? = null

    fun retrofit(dataStore: DataStore) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient(AppInterceptor(dataStore))) // okHttpClient를 Retrofit 빌더에 추가
        .build()

    fun okHttpClient(interceptor: AppInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(interceptor) // preference에 저장된 jwt 토큰을 헤더에 자동으로 입력
            .build()
    }

    class AppInterceptor(private val dataStore: DataStore) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            CoroutineScope(Dispatchers.Main).launch {
                jwtToken = dataStore.getToken().first()
            }

            val newRequest = request().newBuilder()
                .addHeader("Authorization", jwtToken ?: "")
                .build()
            proceed(newRequest)
        }
    }

}
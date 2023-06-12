package com.umc.oppla

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.umc.oppla.data.local.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ApplicationClass  : Application() {

    private lateinit var dataStore : DataStore
    fun getDataStore() : DataStore = dataStore

    override fun onCreate() {
        super.onCreate()
//        // 다른 초기화 코드들
//        val client: OkHttpClient = OkHttpClient.Builder()
//            .readTimeout(30000, TimeUnit.MILLISECONDS)
//            .connectTimeout(30000, TimeUnit.MILLISECONDS)
//            .addNetworkInterceptor(XAccessTokenInterceptor()) // preference에 저장된 jwt 토큰을 헤더에 자동으로 입력
//            .build()
//
//        // ??
//        val gson: Gson = GsonBuilder().setLenient().create()
//
//        retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()

        dataStore = DataStore(this)

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.kakao_api_key)
    }


}
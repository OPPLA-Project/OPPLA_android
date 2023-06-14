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

        dataStore = DataStore(this)
        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.kakao_api_key)
    }

}
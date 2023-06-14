package com.umc.oppla.widget.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService

object Utils {
    const val KEY_PERMISSION_DATA = "key_permission_data" // background permission data를 기록하기 위함
    const val BACKGROUND_PERMISSION_DATA = "background_permission_data"

    const val SHARED_SEARCH_HISTORY = "shared_search_histroy" // 검색기록을 저장하는 sharedpref를 찾을 때 필요한 key값
    const val KEY_SEARCH_HISTORY = "key_search_history" // 검색기록을 저장하는 sharedpref에서 검색기록을 찾을 때 필요한 key값

    const val KAKAO_MAP_URL = "https://dapi.kakao.com/"
    const val KAKAO_REST_API_KEY = "KakaoAK df6a8c865ce23b81d7f7f5285792330f"  // REST API 키

    const val BASE_URL = "http://oppla-env.eba-iuykwqzn.ap-northeast-2.elasticbeanstalk.com/" // API 의 base_url
//    lateinit var retrofit: Retrofit
    const val TAG = "whatisthis"

    // 키보드 숨기기
    fun hidekeyboard(activity: Activity){
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.applicationWindowToken,
            0)
    }
}
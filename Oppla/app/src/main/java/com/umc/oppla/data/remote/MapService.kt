package com.umc.oppla.data.remote

import com.umc.oppla.data.model.ResultSearchKeyword
import com.umc.oppla.data.model.ResultSearchLatLng
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MapService {
    @GET("v2/local/search/keyword.json") // Keyword.json의 정보를 받아옴
    fun getSearchKeyword(
        @Header("Authorization") key: String, // 카카오 API 인증키 [필수]
        @Query("query") query: String // 검색을 원하는 질의어 [필수]
    ): Call<ResultSearchKeyword> // 받아온 정보가 ResultSearchKeyword 클래스의 구조로 담김

    // GET /v2/local/geo/coord2address.${FORMAT} HTTP/1.1
    @GET("v2/local/geo/coord2address.json") // Keyword.json의 정보를 받아옴
    fun getSearchLatLng(
        @Header("Authorization") key: String, // 카카오 API 인증키 [필수]
        @Query("x") x: String, // lat
        @Query("y") y: String // lng
    ): Call<ResultSearchLatLng> // 받아온 정보가 ResultSearchKeyword 클래스의 구조로 담김

}
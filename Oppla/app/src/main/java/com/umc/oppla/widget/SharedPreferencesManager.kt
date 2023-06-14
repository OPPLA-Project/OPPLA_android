package com.umc.oppla.widget

import android.content.Context
import android.content.SharedPreferences
import com.umc.oppla.widget.utils.Utils.BACKGROUND_PERMISSION_DATA

class SharedPreferencesManager(context:Context) {

    // // 쉐어드 만들기
    private var backgroundpermission: SharedPreferences =
        context.getSharedPreferences(BACKGROUND_PERMISSION_DATA, Context.MODE_PRIVATE)

    // permission 데이터 출력
    fun getPermission(key:String): Boolean = backgroundpermission.getBoolean(key, false)

    // permission 데이터 저장
    fun setPermission(key : String, permission: Boolean) {
        backgroundpermission.edit().putBoolean(key, permission).apply()
    }

    // permission 데이터 삭제
    fun deletePermission(){
        backgroundpermission.edit().clear().apply()
    }

}
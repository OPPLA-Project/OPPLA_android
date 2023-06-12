package com.umc.oppla.repository

import com.umc.oppla.data.local.DataStore
import com.umc.oppla.data.model.auth.AuthRequest
import com.umc.oppla.data.model.auth.AuthResponse
import com.umc.oppla.data.remote.LoginService
import com.umc.oppla.widget.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class LoginRepository(private val loginService: LoginService, private val datastore: DataStore) {
    suspend fun create(jsonparams: AuthRequest): Flow<NetworkResult<AuthResponse>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = loginService.post_users(jsonparams)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(NetworkResult.Success(it))
                }
            } else {
                try {
                    emit(NetworkResult.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: ""))
        } as Unit
    }.flowOn(Dispatchers.IO)

    suspend fun saveToken(token: String) = datastore.saveToken(token)

    fun getToken() = datastore.getToken()

    //    fun create(jsonparams: AuthRequest) {
//        loginService.post_users(jsonparams).enqueue(object : Callback<AuthResponse> {
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//                val resp = response.body()
//                Log.d(Utils.TAG, "response" + response.body().toString())
//
////                ApplicationClass.sharedPreferencesmanager.setJwt(ApplicationClass.X_ACCESS_TOKEN, resp?.appToken.toString())
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                Log.d(Utils.TAG, "네트워크 오류가 발생했습니다." + t.message.toString())
//            }
//        })
//    }

}
package com.umc.oppla.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.oppla.data.model.auth.AuthRequest
import com.umc.oppla.data.model.auth.AuthResponse
import com.umc.oppla.repository.LoginRepository
import com.umc.oppla.widget.utils.NetworkResult
import kotlinx.coroutines.launch

class LoginViewModel constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private var _jwt_data = MutableLiveData<NetworkResult<AuthResponse>>()
    val jwt_data: LiveData<NetworkResult<AuthResponse>> get() = _jwt_data

    fun create(jsonparams: AuthRequest) {
        viewModelScope.launch {
            loginRepository.create(jsonparams).collect {
                _jwt_data.value = it
            }
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            loginRepository.saveToken(token)
        }
    }

    fun getToken() {
        viewModelScope.launch {
            loginRepository.getToken()
        }
    }

}
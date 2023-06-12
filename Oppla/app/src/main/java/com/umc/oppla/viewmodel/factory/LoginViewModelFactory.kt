package com.umc.oppla.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umc.oppla.repository.LoginRepository
import com.umc.oppla.viewmodel.LoginViewModel

class LoginViewModelFactory(val loginRepository: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(loginRepository) as T
    }
}
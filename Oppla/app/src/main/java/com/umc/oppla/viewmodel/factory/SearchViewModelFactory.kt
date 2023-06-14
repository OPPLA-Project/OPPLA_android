package com.umc.oppla.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umc.oppla.repository.SearchRepository
import com.umc.oppla.viewmodel.SearchViewModel

class SearchViewModelFactory(val searchRepository: SearchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(searchRepository) as T
    }
}
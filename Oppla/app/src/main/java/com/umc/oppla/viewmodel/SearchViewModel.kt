package com.umc.oppla.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    var _searchword_data = MutableLiveData<String>()
    val searchword_data: LiveData<String> get() = _searchword_data

    fun set_searchword_data(searchword: String) {
        _searchword_data.value = searchword
    }
}
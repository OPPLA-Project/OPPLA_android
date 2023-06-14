package com.umc.oppla.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.oppla.repository.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {

    var _searchword_data = MutableLiveData<String>()
    val searchword_data: LiveData<String> get() = _searchword_data

    fun resetSearchWord() {
        _searchword_data = MutableLiveData<String>()
    }

    fun setSearchWord(searchword: String) {
        _searchword_data.postValue(searchword)
    }

    private var _searchhistroy = MutableLiveData<Set<String>>()
    val searchhistroy: LiveData<Set<String>> get() = _searchhistroy

    fun saveSearchHistory(searchword: String) {
        viewModelScope.launch {
            searchRepository.saveSearchHistory(searchword)
        }
    }

    fun deleteSearchHistory(searchword: String) {
        viewModelScope.launch {
            searchRepository.deleteSearchHistory(searchword)
        }
    }

    fun getSearchHistory() {
        viewModelScope.launch {
            searchRepository.getSearchHistory().collect { it ->
                _searchhistroy.postValue(it)
            }
        }
    }

    private var _searchBTstate = MutableLiveData<Boolean>()
    val searchBTstate: LiveData<Boolean> get() = _searchBTstate

    fun setSearchBTstate() {
        if (_searchBTstate.value == true) {
            _searchBTstate.postValue(false)
        } else {
            _searchBTstate.postValue(true)
        }
    }

    fun resetSearchBTstate() {
        _searchBTstate.postValue(true)
    }


}
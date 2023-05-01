package com.umc.oppla.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel(){

    private var _mylocation = MutableLiveData<Pair<Double, Double>>()
    val mylocation: LiveData<Pair<Double, Double>>
        get() = _mylocation

    fun getMyLocation(location: Pair<Double, Double>) {
        _mylocation.value = location
    }

    private var _searchlocation = MutableLiveData<Pair<Double, Double>>()
    val searchlocation: LiveData<Pair<Double, Double>>
        get() = _searchlocation

    fun getSearchLocation(location: Pair<Double, Double>) {
        _searchlocation.value = location
    }

}
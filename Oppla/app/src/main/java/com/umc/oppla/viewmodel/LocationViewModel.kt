package com.umc.oppla.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umc.oppla.data.remote.model.ResultSearchLatLng

class LocationViewModel : ViewModel(){

    private var _mylocation = MutableLiveData<Pair<Double, Double>>()
    val mylocation: LiveData<Pair<Double, Double>>
        get() = _mylocation

    fun setMyLocation(location: Pair<Double, Double>) {
        _mylocation.value = location
    }

    private var _searchlocation = MutableLiveData<Pair<Double, Double>>()
    val searchlocation: LiveData<Pair<Double, Double>>
        get() = _searchlocation

    fun setSearchLocation(location: Pair<Double, Double>) {
        _searchlocation.value = location
    }

    private var _displaylocation = MutableLiveData<Pair<Double, Double>>()
    val displaylocation: LiveData<Pair<Double, Double>>
        get() = _displaylocation

    fun setDisplayLocation(location: Pair<Double, Double>) {
        _displaylocation.value = location
    }


    private var _doquestionlocation = MutableLiveData<ResultSearchLatLng>()
    val doquestionlocation: LiveData<ResultSearchLatLng>
        get() = _doquestionlocation

    fun setDoquestionLocation(location: ResultSearchLatLng) {
        _doquestionlocation.value = location
    }

//    // api 호출해서 데이터를 얻어오면 됨
//    private var _doanswerlocation = MutableLiveData<Pair<Double, Double>>()
//    val doanswerlocation: LiveData<Pair<Double, Double>>
//        get() = _doanswerlocation
//
//    fun getDoanswerLocation(location: Pair<Double, Double>) {
//        _doanswerlocation.value = location
//    }
}
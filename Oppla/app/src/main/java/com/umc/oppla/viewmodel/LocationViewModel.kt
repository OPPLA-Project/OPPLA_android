package com.umc.oppla.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel(){

    private var _mylocation = MutableLiveData<Location>()
    val mylocation: LiveData<Location>
        get() = _mylocation

    fun getMyLocation(location: Location) {
        _mylocation.value = location
    }


}
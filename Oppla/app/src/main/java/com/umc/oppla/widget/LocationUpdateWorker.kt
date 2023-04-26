package com.umc.oppla.widget

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
class LocationUpdateWorker(Context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(Context, workerParams) {

    private val context = Context
    //위치 가져올때 필요
    private val locationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    companion object{
        private var _MyLocation : MutableLiveData<Location> = MutableLiveData()
        val MyLocation: LiveData<Location>
            get() = _MyLocation

    }

    override suspend fun doWork(): Result {
        return try {
            checkDistance()
        } catch (exception: Exception) {
            Log.d("whatisthis", exception.toString())
            Result.failure()
        }
    }

    private fun checkDistance(): Result {
        try {
            //마지막 위치를 가져오는데에 성공한다면
            locationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let { aLocation ->
                        _MyLocation.value = aLocation
                        // api 호출(갱신)
                        Log.d("whatisthis",aLocation.toString())
                    }
                } else {
                    Log.d("whatisthis", "task 실패")
                }
            }
        } catch (err: SecurityException) {
            Log.d("whatisthis", err.toString())
            return Result.failure()
        }
        return Result.success()
    }

//    fun findLastLocation():Pair<Double,Double>?{
//        return if(MyLocation!=null) MyLocation
//        else null
//    }
}
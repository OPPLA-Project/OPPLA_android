package com.umc.oppla.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
class LocationUpdateWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private val mContext = appContext
    //위치 가져올때 필요
    private val locationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(mContext)
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
                        val fromLat = aLocation.latitude
                        val fromLng = aLocation.longitude
                        Log.d("whatisthis", "$fromLat $fromLng")
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
}
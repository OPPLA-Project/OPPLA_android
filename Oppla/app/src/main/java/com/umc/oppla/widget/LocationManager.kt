package com.umc.oppla.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.os.Looper
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class LocationManager(
    Context: Context, private var timeInterval: Long,
    private var minimalDistance: Float
) : LocationCallback() {

    private val context = Context
    private lateinit var requestInApp: LocationRequest
    private var locationClient: FusedLocationProviderClient

    init {
        // getting the location client
        locationClient = LocationServices.getFusedLocationProviderClient(context)
        initLocationClientInApp()
    }

    @SuppressLint("MissingPermission")
    private fun initLocationClientInApp() {
        requestInApp =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
                setMinUpdateDistanceMeters(minimalDistance) // 위치를 업데이트할 때 요구되는 최소한의 거리 변화를 설정함
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL) // 위치 세분성 : 권한 수준과 일치함
                setWaitForAccurateLocation(true) // PRIORITY_HIGH_ACCURACY일 경우 정확한 위치가 올 때까지 지연될 수 있음
            }.build()

        taskbuild(requestInApp)
    }

    private fun taskbuild(request: LocationRequest) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(request)
        val client = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            Log.d("whatisthis", "location client setting success")
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
//                    exception.startResolutionForResult(REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    fun changeRequest(timeInterval: Long, minimalDistance: Float) {
        this.timeInterval = timeInterval
        this.minimalDistance = minimalDistance
        initLocationClientInApp()
        stopLocationTracking()
        startLocationTracking()
    }

    @SuppressLint("MissingPermission")
    fun startLocationTracking() =
        locationClient.requestLocationUpdates(requestInApp, this, Looper.getMainLooper())


    fun stopLocationTracking() {
        locationClient.flushLocations()
        locationClient.removeLocationUpdates(this)
    }

    override fun onLocationResult(result: LocationResult) { // 위치 정보 반환 받았을 경우
        super.onLocationResult(result)
        Log.d("whatisthis", result.toString())
    }
}
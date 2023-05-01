package com.umc.oppla.view.main

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.kakao.util.maps.helper.Utility
import com.umc.oppla.R
import com.umc.oppla.base.BaseActivity
import com.umc.oppla.databinding.ActivityMainBinding
import com.umc.oppla.view.main.answer.AnswerBlankFragment
import com.umc.oppla.view.main.home.HomeBlankFragment
import com.umc.oppla.view.main.mypage.MypageBlankFragment
import com.umc.oppla.view.main.question.QuestionBlankFragment
import com.umc.oppla.viewmodel.LocationViewModel
import com.umc.oppla.viewmodel.SearchViewModel
import com.umc.oppla.widget.LocationManager
import com.umc.oppla.widget.LocationUpdateWorker
import com.umc.oppla.widget.SharedPreferencesManager
import com.umc.oppla.widget.utils.Utils.KEY_PERMISSION_DATA
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,  // 도시 블록 단위
            Manifest.permission.ACCESS_FINE_LOCATION  // 더 정밀한 단위
        )

        @RequiresApi(Build.VERSION_CODES.Q)
        private val REQUIRED_PERMISSION_BACKGROUND = Manifest.permission.ACCESS_BACKGROUND_LOCATION
    }

    private lateinit var sharedPreferencesmanager: SharedPreferencesManager
    private lateinit var currentFragmenttag: String
    private lateinit var locationManager: LocationManager

    lateinit var locationViewModel: LocationViewModel
    lateinit var searchViewModel: SearchViewModel

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentFragmenttag = savedInstanceState.get("homeblank").toString()
    }

    override fun savedatainit() {
        supportFragmentManager
            .beginTransaction()
            .add(binding.mainFragmentContainerViewContainer.id, HomeBlankFragment(), "homeblank")
            .commitAllowingStateLoss()
        currentFragmenttag = "homeblank" // 현재 보고 있는 fragmet의 Tag
    }


    override fun init() {
        locationViewModel = ViewModelProvider(this@MainActivity).get(LocationViewModel::class.java)
        searchViewModel = ViewModelProvider(this@MainActivity).get(SearchViewModel::class.java)
        sharedPreferencesmanager = SharedPreferencesManager(this)

        // 권한 묻기
        if (!isAllPermissionsGranted()) { // 위치 권한 없을 때(foreground이므로 background 제외)
            requestLocationPermission() // 처음 물어볼 때는 background도 물어보자
        } else { // 위치 권한 있을 때(foreground이므로 background 제외)
            // 위치 정보를 바탕으로 데이터 요청
            getLocationData()
        }
        // 위치 정보가 갱신되면 데이터 재요청(viewmodel로 변화 감지)

        // 네비게이션 버튼의 테마색으로 변하는 것을 막기 위해서
        binding.mainBottomnavigationBnb.itemIconTintList = null

        // 네비게이션 버튼 클릭시 프래그먼트 전환
        binding.mainBottomnavigationBnb.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeBaseFragment -> { // 첫 번째 fragment
                    changeFragment("homeblank", HomeBlankFragment())
                }
                R.id.favoriteBaseFragment -> { // 두 번째 fragment
                    changeFragment("questionblank", QuestionBlankFragment())
                }
                R.id.locationFragment -> { // 세 번째 fragment
                    changeFragment("answerblank", AnswerBlankFragment())
                }
                R.id.chatFragment -> { // 세 번째 fragment
                    changeFragment("mypageblank", MypageBlankFragment())
                }
            }
            true
        }
    }

    private fun changeFragment(tag: String, fragment: Fragment) {
        // supportFragmentManager에 "first"라는 Tag로 저장된 fragment 있는지 확인
        if (supportFragmentManager.findFragmentByTag(tag) == null) { // Tag가 없을 때 -> 없을 리가 없다.
            supportFragmentManager
                .beginTransaction()
                .hide(supportFragmentManager.findFragmentByTag(currentFragmenttag)!!)
                .add(binding.mainFragmentContainerViewContainer.id, fragment, tag)
                .commitAllowingStateLoss()

        } else { // Tag가 있을 때
            // 먼저 currentFragmenttag에 저장된 '이전 fragment Tag'를 활용해 이전 fragment를 hide 시킨다.
            // supportFragmentManager에 저장된 "first"라는 Tag를 show 시킨다.
            supportFragmentManager
                .beginTransaction()
                .hide(supportFragmentManager.findFragmentByTag(currentFragmenttag)!!)
                .show(supportFragmentManager.findFragmentByTag(tag)!!)
                .commitAllowingStateLoss()
        }
        // currentFragmenttag에 '현재 fragment Tag' "first"를 저장한다.
        currentFragmenttag = tag
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    // 위치 정보 요청
    private fun getLocationData() {
        if (sharedPreferencesmanager.getPermission(KEY_PERMISSION_DATA)) { // background 허용 -> 앱을 껐을 때도 작동
            checkBackgroundPermission()
        } else { // background x -> 앱을 켰을 때만
            locationManager = LocationManager(this, 1200000, 1f) // 20분마다
            locationManager.startLocationTracking()
            locationManager.MyLocation.observe(this, Observer {
                if (it != null) {
                    Log.d("whatisthis", "데이터 갱신됨 $it")
                    locationViewModel.getMyLocation(Pair(it.latitude, it.longitude))
                }
            })
        }
    }

    private fun isAllPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestLocationPermissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

    //위치 권한
    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var isGranted = true
        permissions.entries.forEach { permission ->
            when {
                permission.value -> {}
                ActivityCompat.shouldShowRequestPermissionRationale(this, permission.key) -> {
                    Toast.makeText(this, "내 주변 질문들을 볼 수 없습니다.", Toast.LENGTH_SHORT)
                        .show()
                    // 권한이 필요한 이유 설명 후, 한번 더 권한 물어보기
                    isGranted = false
                    return@registerForActivityResult
                }
                else -> isGranted = false
                // 세팅으로 넘기기(무조건 필요한 경우에만)
            }
        }

        if (isGranted) {
            // Check background permission android Q
            permissionDialog(this)
        }
    }

    // 백그라운드 권한이 필요한 이유를 설명할 dialog
    private fun permissionDialog(context: Context) {
        var builder = AlertDialog.Builder(context)
        builder.setTitle("앱이 꺼졌을 때도 질문을 받기 위해선 위치 권한을 항상 허용으로 해야 합니다.")
        var listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE -> {
                    checkBackgroundPermission()
                    // 안드로이드 10 미만에는 background 허용 구분이 없기 때문에 이걸로 구분한다.
                    sharedPreferencesmanager.setPermission(KEY_PERMISSION_DATA, true)
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    getLocationData()
                    // 안드로이드 10 미만에는 background 허용 구분이 없기 때문에 이걸로 구분한다.
                    sharedPreferencesmanager.setPermission(KEY_PERMISSION_DATA, false)
                }
            }
        }
        builder.setPositiveButton("네", listener)
        builder.setNegativeButton("아니오", listener)
        builder.show()
    }

    // 권한 체크
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun isAccessBackgroundGrantedAllTime(): Boolean = ContextCompat.checkSelfPermission(
        this,
        REQUIRED_PERMISSION_BACKGROUND
    ) == PackageManager.PERMISSION_GRANTED

    // 백그라운드 권한 요청
    private fun checkBackgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // 안드로이드 10 이상일 경우
            if (!isAccessBackgroundGrantedAllTime()) { // background 권한 허용안됨
                openSettingBackgroundMode(requestBackgroundPermission)
            } else {
                doWorkWithPeriodic()
            }
        } else { // background 권한 필요 없음
            doWorkWithPeriodic()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun openSettingBackgroundMode(result: ActivityResultLauncher<String>) {
        result.launch(REQUIRED_PERMISSION_BACKGROUND)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private val requestBackgroundPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permission ->
        when {
            permission -> { // background 허용
                doWorkWithPeriodic() // 위치 정보 얻어오기
                sharedPreferencesmanager.setPermission(
                    KEY_PERMISSION_DATA,
                    true
                ) // background 허용했음을 저장
            }
            shouldShowRequestPermissionRationale(REQUIRED_PERMISSION_BACKGROUND) -> {
                sharedPreferencesmanager.setPermission(KEY_PERMISSION_DATA, false)
                Toast.makeText(this, "앱이 꺼졌을 때도 질문을 받을려면 권한이 필요합니다.", Toast.LENGTH_LONG)
                    .show()
//                // 권한이 필요한 이유 설명 후, 한번 더 권한 물어보기
//                return@registerForActivityResult
            }
            else -> {
                // 세팅으로 넘기기(무조건 필요한 경우에만)
                openSettings() // 세번 째부터는 강제로 열어야 한다.
                sharedPreferencesmanager.setPermission(KEY_PERMISSION_DATA, false)
                Toast.makeText(this, "앱 껐을 때 질문 x", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    // 20분 간격으로 위치 정보를 얻어온다 (background에서도)
    private fun doWorkWithPeriodic() {
        Log.d("whatisthis", "worker 시작함수 진입")

        val workRequest =
            PeriodicWorkRequestBuilder<LocationUpdateWorker>(15, TimeUnit.MINUTES).build() // 20분마다

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("work", ExistingPeriodicWorkPolicy.REPLACE, workRequest)

        LocationUpdateWorker.MyLocation.observe(this, Observer {
            if(it!=null){
                Log.d("whatisthis","백그라운드 데이터 갱신됨 $it")

                locationViewModel.getMyLocation(Pair(it.latitude, it.longitude))
            }
        })
    }

    // 설정 페이지로 이동시켜줌
    private fun openSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", packageName, null)
        }.run(::startActivity)
    }
}
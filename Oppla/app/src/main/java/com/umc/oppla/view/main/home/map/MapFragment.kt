package com.umc.oppla.view.main.home.map

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.data.MarkerDataTemp
import com.umc.oppla.data.remote.MapService
import com.umc.oppla.data.model.ResultSearchLatLng
import com.umc.oppla.databinding.FragmentMapBinding
import com.umc.oppla.view.main.MainActivity
import com.umc.oppla.view.main.home.doquestion.DoquestionFragment
import com.umc.oppla.view.main.home.search.SearchFragment
import com.umc.oppla.viewmodel.LocationViewModel
import com.umc.oppla.widget.utils.Utils
import net.daum.mf.map.api.MapCircle
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.MapViewEventListener
import net.daum.mf.map.api.MapView.POIItemEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


class MapFragment : BaseFragment<FragmentMapBinding>(com.umc.oppla.R.layout.fragment_map),
    MapViewEventListener, POIItemEventListener {
    // 툴바 메뉴
    private lateinit var toolbarmenu: Menu

    lateinit var locationViewModel: LocationViewModel

    // bottomsheet(매장 상세 정보)
    private lateinit var bottomSheetBehaviorDoquestion : BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorDoanswer: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorMyquestion: BottomSheetBehavior<LinearLayout>

    // 질문이 들어가는 범위 보여주기
    private lateinit var DoquestionCircle : MapCircle

    private val NowMarkers = mutableListOf<MapPOIItem>()

    override fun init() {

        initAppbar(binding.mapToolbar, R.menu.menu_map, false, "현재 위치")
        initAppbarItem()


        binding.apply {
            locationViewModel = (activity as MainActivity).locationViewModel

            mapMapview.setMapViewEventListener(this@MapFragment)
            mapMapview.setPOIItemEventListener(this@MapFragment)

            initializePersistentBottomSheet()

            locationViewModel.searchlocation.observe(this@MapFragment, Observer {
                if (it != null) {
                    Log.d("whatisthis", "searchlocation in map : $it")
                    mapMapview.setMapCenterPoint(
                        MapPoint.mapPointWithGeoCoord(
                            it.first,
                            it.second
                        ), false
                    )
                }
            })

            locationViewModel.mylocation.observe(this@MapFragment, Observer {
                if (it != null) {
                    Log.d("whatisthis", "mylocation in map : $it")
                    mapMapview.setMapCenterPoint(
                        MapPoint.mapPointWithGeoCoord(
                            it.first,
                            it.second
                        ), false
                    )

                    // 내가 볼 수 있는 질문 범위 보여주기
                    val circle1 = MapCircle(
                        mapPointWithGeoCoord(it.first, it.second),  // center
                        500,  // radius
                        Color.argb(100, 239, 245, 255),  // strokeColor
                        Color.argb(100, 61, 164, 254) // fillColor
                    )
                    circle1.tag = 1234
                    mapMapview.addCircle(circle1)

                    // 테스트용-----------------------------------------------------------------------
                    val marker = MapPOIItem()
                    marker.apply {
                        itemName = "Default Marker"
                        tag = 0
                        isShowCalloutBalloonOnTouch = false // 풍선 없앰
                        mapPoint = mapPointWithGeoCoord(it.first, it.second)
                        markerType = MapPOIItem.MarkerType.CustomImage // 기본으로 제공하는 BluePin 마커 모양.
                        customImageResourceId = R.drawable.icon_marker_myquestion_noanswer
                        selectedMarkerType =
                            MapPOIItem.MarkerType.CustomImage // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                        customSelectedImageResourceId = R.drawable.icon_marker_myquestion_answer
                    }
                    mapMapview.addPOIItem(marker)
                    //------------------------------------------------------------------------------
                }

                mapImageviewCenterpoint.setOnClickListener {
                    bottomSheetBehaviorDoanswer.state = BottomSheetBehavior.STATE_EXPANDED
                }

                mapTextviewDoquestion.setOnClickListener {
                    // 위,경도 정보로 위치 검색 api 호출
                    searchLatLng(locationViewModel.displaylocation.value!!.second, locationViewModel.displaylocation.value!!.first)
                    // 질문 페이지로
                    parentFragmentManager
                        .beginTransaction()
                        .add(R.id.homeblank_layout, DoquestionFragment(), "doqeustion")
                        .addToBackStack("map")
                        .commitAllowingStateLoss()
                    // 원 지우기
                    mapMapview.removeCircle(DoquestionCircle)
                }
            })
        }
    }

    private fun updateMarker(storelist: ArrayList<MarkerDataTemp>) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        val markerlistTemp = mutableListOf<MapPOIItem>()
        // 백그라운드 쓰레드
        executor.execute {
            for (storeData in storelist) { // List 중 하나 선택
                val marker = MapPOIItem()
                marker.itemName = "Default Marker"
                marker.tag = 0
                marker.mapPoint = mapPointWithGeoCoord(storeData.lat, storeData.lng)
//                marker.markerType = MapPOIItem.MarkerType.CustomImage // 기본으로 제공하는 BluePin 마커 모양.
                marker.customImageResourceId = R.drawable.icon_marker_myquestion_noanswer
                marker.selectedMarkerType =
                    MapPOIItem.MarkerType.CustomImage // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                marker.customSelectedImageResourceId = R.drawable.icon_marker_myquestion_answer

                markerlistTemp.add(marker) // 추가된 마커만 그리기 위해서
                NowMarkers.add(marker)
            }
            // 메인 스레드
            handler.post {
                for (marker in markerlistTemp) {
                    binding.mapMapview.addPOIItem(marker)
                }
            }
        }
    }

    override fun onMapViewInitialized(p0: MapView?) {
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
        // 중심 좌표가 변경된 경우
        bottomSheetBehaviorDoquestion.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.mapMapview.removeCircle(DoquestionCircle)
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
        binding.mapMapview.removeCircle(DoquestionCircle)
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        // 그냥 지도를 터치했을 경우
        // 질문하기를 제외한 열려있는 모든 bottomsheet 없애기
        bottomSheetBehaviorDoanswer.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehaviorMyquestion.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.mapMapview.removeCircle(DoquestionCircle)
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
        // 사용자가 드래그를 시작했을 경우
//        bottomSheetBehaviorDoquestion.state = BottomSheetBehavior.STATE_COLLAPSED
//        binding.mapMapview.removeCircle(DoquestionCircle)
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        //사용자가 지도 드래그를 끝낸 경우 호출된다. -> 드래그가 끝나면 내가 터치한 곳의 좌표를 알려준다.
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
        // 지도의 이동이 완료된 경우 호출된다. -> 이동이 끝나면 화면 중심 좌표를 알려준다.
        DoquestionCircle = MapCircle(
            mapPointWithGeoCoord(p1?.mapPointGeoCoord?.latitude!!, p1.mapPointGeoCoord.longitude),  // center
            500,  // radius
            Color.argb(100, 239, 245, 255),  // strokeColor
            Color.argb(100, 31, 110, 245) // fillColor
        )
        DoquestionCircle.tag = 5678
        binding.mapMapview.addCircle(DoquestionCircle)

        bottomSheetBehaviorDoquestion.state = BottomSheetBehavior.STATE_EXPANDED

        // 위, 경도 정보 갱신
        locationViewModel.setDisplayLocation(Pair(p1.mapPointGeoCoord.latitude, p1.mapPointGeoCoord.longitude))
    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
        // 받은 질문 마커 클릭 시
        bottomSheetBehaviorDoanswer.state = BottomSheetBehavior.STATE_EXPANDED
        // 내가 한 질문 마커 클릭 시

    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
        // 말풍선 클릭 시 (Deprecated)
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
        // 말풍선 클릭 시
    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
        // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
    }

    // bottomsheet(매장 상세 정보)
    private fun initializePersistentBottomSheet() {
        val dm = resources.displayMetrics

        // BottomSheetBehavior에 layout 설정
        // 지도 이동이 멈췄을 때
        bottomSheetBehaviorDoquestion = BottomSheetBehavior.from(binding.mapBottomsheetlayoutDoquestion)
        // 미리보기 높이 = 현재 위치 버튼 크기 + 로고 크기 + 마진 크기
        bottomSheetBehaviorDoquestion.peekHeight = 0
        bottomSheetBehaviorDoquestion.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // BottomSheetBehavior state에 따른 이벤트
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        // BottomSheetBehavior에 layout 설정
        // 들어온 질문 마커를 클릭 시
        bottomSheetBehaviorDoanswer = BottomSheetBehavior.from(binding.mapBottomsheetlayoutDoanswer)
        // 미리보기 높이 = 현재 위치 버튼 크기 + 로고 크기 + 마진 크기
        bottomSheetBehaviorDoanswer.peekHeight = 0

        bottomSheetBehaviorDoanswer.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // BottomSheetBehavior state에 따른 이벤트
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        // BottomSheetBehavior에 layout 설정
        // 내가 한 질문 클릭 시
        bottomSheetBehaviorMyquestion =
            BottomSheetBehavior.from(binding.mapBottomsheetlayoutMyquestion)
        // 미리보기 높이 = 현재 위치 버튼 크기 + 로고 크기 + 마진 크기
        bottomSheetBehaviorMyquestion.peekHeight = 0

        bottomSheetBehaviorMyquestion.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // BottomSheetBehavior state에 따른 이벤트
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    // 키워드 검색 함수
    private fun searchLatLng(lat:Double, lng:Double) {
        val retrofit = Retrofit.Builder() // Retrofit 구성
            .baseUrl(Utils.KAKAO_MAP_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(MapService::class.java) // 통신 인터페이스를 객체로 생성
        val call = api.getSearchLatLng(Utils.KAKAO_REST_API_KEY.toString(), lat.toString(), lng.toString()) // 검색 조건 입력

        // API 서버에 요청
        call.enqueue(object : Callback<ResultSearchLatLng> {
            override fun onResponse(
                call: Call<ResultSearchLatLng>,
                response: Response<ResultSearchLatLng>
            ) {
                if(response.isSuccessful){
                    Log.d("whatisthis","주소정보 : ${response.body()?.meta}")
                    locationViewModel.setDoquestionLocation(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ResultSearchLatLng>, t: Throwable) {
                // 통신 실패
                Log.w("whatisthis", "통신 실패: ${t.message}")
            }
        })
    }

    override fun initAppbarItem() {
        toolbarmenu = baseToolbar.menu

        // 검색 버튼 클릭 시
        val toolbarsearch = toolbarmenu.findItem(R.id.map_menu_search)
        toolbarsearch.setOnMenuItemClickListener {
            parentFragmentManager
                .beginTransaction()
                .add(R.id.homeblank_layout, SearchFragment(), "search")
                .addToBackStack("map")
                .commitAllowingStateLoss()
            true
        }

        // 알림 버튼 클릭 시
        val toolbarnotification = toolbarmenu.findItem(R.id.map_menu_notification)
        toolbarnotification.setOnMenuItemClickListener {

            true
        }

    }
}
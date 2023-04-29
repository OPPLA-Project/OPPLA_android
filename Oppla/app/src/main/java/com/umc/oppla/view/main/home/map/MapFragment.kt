package com.umc.oppla.view.main.home.map

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.data.MarkerDataTemp
import com.umc.oppla.databinding.FragmentMapBinding
import com.umc.oppla.view.main.MainActivity
import com.umc.oppla.view.main.home.doquestion.DoquestionFragment
import com.umc.oppla.viewmodel.LocationViewModel
import net.daum.mf.map.api.MapCircle
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.MapViewEventListener
import net.daum.mf.map.api.MapView.POIItemEventListener
import java.util.concurrent.Executors


class MapFragment : BaseFragment<FragmentMapBinding>(com.umc.oppla.R.layout.fragment_map),
    MapViewEventListener, POIItemEventListener {
    lateinit var locationViewModel: LocationViewModel
    // bottomsheet(매장 상세 정보)
    private lateinit var bottomSheetBehaviorDoanswer: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorMyquestion: BottomSheetBehavior<LinearLayout>

    private val NowMarkers = mutableListOf<MapPOIItem>()

    override fun init() {
        binding.mapTextviewDoquestion.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .add(R.id.homeblank_layout, DoquestionFragment(), "doqeustion")
                .addToBackStack("map")
                .commitAllowingStateLoss()
        }

        binding.apply {
            mapMapview.setMapViewEventListener(this@MapFragment)
            mapMapview.setPOIItemEventListener(this@MapFragment)
            initializePersistentBottomSheet()

            locationViewModel = (activity as MainActivity).locationViewModel
            locationViewModel.mylocation.observe(this@MapFragment, Observer {
                if (it != null) {
                    Log.d("whatisthis", "in map : $it")
                    mapMapview.setMapCenterPoint(
                        MapPoint.mapPointWithGeoCoord(
                            it.latitude,
                            it.longitude
                        ), false
                    )

                    val circle1 = MapCircle(
                        mapPointWithGeoCoord(it.latitude, it.longitude),  // center
                        500,  // radius
                        Color.argb(128, 0,191,255),  // strokeColor
                        Color.argb(120, 130,200,230) // fillColor
                    )
                    circle1.tag = 1234
                    binding.mapMapview.addCircle(circle1)

                    // 테스트용-----------------------------------------------------------------------
                    val marker = MapPOIItem()
                    marker.itemName = "Default Marker"
                    marker.tag = 0
                    marker.mapPoint = mapPointWithGeoCoord(it.latitude, it.longitude)
                    marker.markerType = MapPOIItem.MarkerType.CustomImage // 기본으로 제공하는 BluePin 마커 모양.
                    marker.customImageResourceId = R.drawable.icon_marker_myquestion_noanswer
                    marker.selectedMarkerType =
                        MapPOIItem.MarkerType.CustomImage // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                    marker.customSelectedImageResourceId = R.drawable.icon_marker_myquestion_answer
                    binding.mapMapview.addPOIItem(marker)
                    //------------------------------------------------------------------------------
                }
            })

            binding.mapImageviewCenterpoint.setOnClickListener {
                bottomSheetBehaviorDoanswer.state =BottomSheetBehavior.STATE_EXPANDED
            }
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
                marker.markerType = MapPOIItem.MarkerType.CustomImage // 기본으로 제공하는 BluePin 마커 모양.
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
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
    }

    // bottomsheet(매장 상세 정보)
    private fun initializePersistentBottomSheet() {
        val dm = resources.displayMetrics
        // BottomSheetBehavior에 layout 설정
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
        bottomSheetBehaviorMyquestion = BottomSheetBehavior.from(binding.mapBottomsheetlayoutMyquestion)
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

}
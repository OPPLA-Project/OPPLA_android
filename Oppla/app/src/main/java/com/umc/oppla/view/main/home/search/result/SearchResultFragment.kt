package com.umc.oppla.view.main.home.search.result

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.data.remote.MapService
import com.umc.oppla.data.model.ResultSearchKeyword
import com.umc.oppla.databinding.FragmentSearchResultBinding
import com.umc.oppla.view.main.MainActivity
import com.umc.oppla.viewmodel.LocationViewModel
import com.umc.oppla.viewmodel.SearchViewModel
import com.umc.oppla.widget.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchResultFragment : BaseFragment<FragmentSearchResultBinding>(R.layout.fragment_search_result) {
    private lateinit var searchresultAdapter: SearchResultAdapter

    lateinit var searchViewModel: SearchViewModel
    lateinit var locationViewModel: LocationViewModel

    override fun init() {
        locationViewModel = (activity as MainActivity).locationViewModel
        searchViewModel = (activity as MainActivity).searchViewModel
        initRecyclerView()
        searchKeyword(searchViewModel.searchword_data.value.toString().trim())
    }

    private fun initRecyclerView(){
        searchresultAdapter =
            SearchResultAdapter(object : SearchResultAdapter.onItemSearchResultClickInterface {
                override fun onItemSearchResultClick(
                    lat: Double,
                    lng: Double,
                    name: String,
                    address: String,
                    position: Int
                ) {
                    locationViewModel.setSearchLocation(Pair(lat,lng))
                    parentFragment!!.parentFragmentManager.popBackStackImmediate(null,0)
                }
            })

        binding.searchresultRecyclerviewResult.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = searchresultAdapter
        }
    }


    // 키워드 검색 함수
    private fun searchKeyword(keyword: String) {
        val retrofit = Retrofit.Builder() // Retrofit 구성
            .baseUrl(Utils.KAKAO_MAP_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(MapService::class.java) // 통신 인터페이스를 객체로 생성
        val call = api.getSearchKeyword(Utils.KAKAO_REST_API_KEY.toString(), keyword) // 검색 조건 입력

        // API 서버에 요청
        call.enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(
                call: Call<ResultSearchKeyword>,
                response: Response<ResultSearchKeyword>
            ) {
                // 통신 성공 (검색 결과는 response.body()에 담겨있음)
//                Log.d("whatisthis", "Raw: ${response.raw()}")
//                Log.d("whatisthis", "Body: ${response.body()}")
                if(response.isSuccessful){
                    searchresultAdapter.submitList(response.body()!!.documents)
                }
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Log.w("whatisthis", "통신 실패: ${t.message}")
            }
        })
    }

    override fun backpress() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (parentFragment!!.parentFragmentManager.backStackEntryCount != 0) {
                    parentFragment!!.parentFragmentManager
                        .popBackStackImmediate(null, 0)
                } else {
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}
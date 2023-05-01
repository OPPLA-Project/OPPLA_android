package com.umc.oppla.view.main.home.search

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.data.remote.MapService
import com.umc.oppla.data.remote.model.ResultSearchKeyword
import com.umc.oppla.databinding.FragmentSearchBinding
import com.umc.oppla.view.main.MainActivity
import com.umc.oppla.view.main.home.search.history.SearchHistroyFragment
import com.umc.oppla.view.main.home.search.result.SearchResultFragment
import com.umc.oppla.viewmodel.SearchViewModel
import com.umc.oppla.widget.utils.Utils.BASE_URL
import com.umc.oppla.widget.utils.Utils.KAKAO_REST_API_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    lateinit var searchViewModel: SearchViewModel

    override fun init() {
    Log.d("whatisthis",parentFragmentManager.backStackEntryCount.toString())
        searchViewModel = (activity as MainActivity).searchViewModel
        binding.searchvm = searchViewModel

        childFragmentManager
            .beginTransaction()
            .replace(R.id.search_innerlayout_result, SearchHistroyFragment(), "searchhistory")
            .commitAllowingStateLoss()

        searchViewModel.searchword_data.observe(this, Observer {
            if (it != null) {
                if (it.length > 0) {
                    childFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.search_innerlayout_result,
                            SearchResultFragment(),
                            "searchresult"
                        )
                        .commitAllowingStateLoss()
                } else {
                    childFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.search_innerlayout_result,
                            SearchHistroyFragment(),
                            "searchhistory"
                        )
                        .commitAllowingStateLoss()
                }
            }
        })
    }
}
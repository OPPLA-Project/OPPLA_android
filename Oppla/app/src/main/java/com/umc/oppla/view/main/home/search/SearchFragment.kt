package com.umc.oppla.view.main.home.search

import androidx.lifecycle.Observer
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentSearchBinding
import com.umc.oppla.view.main.MainActivity
import com.umc.oppla.view.main.home.search.history.SearchHistroyFragment
import com.umc.oppla.view.main.home.search.result.SearchResultFragment
import com.umc.oppla.viewmodel.SearchViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    lateinit var searchViewModel: SearchViewModel

    override fun init() {
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
                            SearchResultFragment(), // 검색 결과
                            "searchresult"
                        )
                        .commitAllowingStateLoss()
                } else {
                    childFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.search_innerlayout_result,
                            SearchHistroyFragment(), // 검색 기록
                            "searchhistory"
                        )
                        .commitAllowingStateLoss()
                }
            }
        })
    }
}
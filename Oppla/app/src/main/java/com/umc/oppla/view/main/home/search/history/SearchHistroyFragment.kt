package com.umc.oppla.view.main.home.search.history

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentSearchHistroyBinding
import com.umc.oppla.view.main.MainActivity
import com.umc.oppla.view.main.home.search.SearchHistoryAdapter
import com.umc.oppla.viewmodel.SearchViewModel
import com.umc.oppla.widget.utils.Utils

class SearchHistroyFragment :
    BaseFragment<FragmentSearchHistroyBinding>(R.layout.fragment_search_histroy) {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchhistoryAdapter: SearchHistoryAdapter

    override fun init() {
        searchViewModel = (activity as MainActivity).searchViewModel
        initRecyclerViewRecord()
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


    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerViewRecord() {
        searchViewModel.getSearchHistory()
        searchViewModel.searchhistroy.observe(this@SearchHistroyFragment, Observer {
            if (it != null) {
                searchhistoryAdapter.submitList(it.toList()
                    .reversed()) // 최근에 입력한 것이 제일 앞에 오게 하기 위해서 reverse 해준다.
            }
        })

        // 기록을 보여줄 recycler의 어댑터, 어댑터 클릭 이벤트 처리
        searchhistoryAdapter =
            SearchHistoryAdapter(object : SearchHistoryAdapter.ItemClickListener {

                // recycler 아이템 중 텍스트를 클릭했을 때 -> 해당 텍스트로 재검색
                override fun onTextItemClick(searchhistory: String, position: Int) {
                    searchViewModel.saveSearchHistory(searchhistory) // 검색 기록 저장
                    searchViewModel.setSearchWord(searchhistory) // 해당 단어로 검색
                    searchViewModel.setSearchBTstate() // 버튼 클릭 상태를 바꾼다(돋보기 -> x) // 기록이 보인다는 것은 아직 아무 것도 입력하지 않았다는 뜻이니까, 당연히 돋보기
                }

                // recycler 아이템 중 x 이미지를 클릭했을 때 -> 데이터 삭제
                @SuppressLint("NotifyDataSetChanged")
                override fun onImageItemClick(searchhistory: String, position: Int) {
                    searchViewModel.deleteSearchHistory(searchhistory)
                }
            })

        binding.searchhistroyRecyclerviewHistory.apply {
            layoutManager = GridLayoutManager(this.context, 3)
            adapter = searchhistoryAdapter
        }
    }
}
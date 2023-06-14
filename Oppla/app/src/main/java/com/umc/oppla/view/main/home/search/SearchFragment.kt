package com.umc.oppla.view.main.home.search

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentSearchBinding
import com.umc.oppla.view.main.MainActivity
import com.umc.oppla.view.main.home.search.history.SearchHistroyFragment
import com.umc.oppla.view.main.home.search.result.SearchResultFragment
import com.umc.oppla.viewmodel.SearchViewModel
import com.umc.oppla.widget.utils.Utils.hidekeyboard

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    lateinit var searchViewModel: SearchViewModel
    override fun init() {
        searchViewModel = (activity as MainActivity).searchViewModel
        searchViewModel.resetSearchWord() // 전에 입력했던 단어를 없앤다.
        searchViewModel.resetSearchBTstate() // 버튼을 클릭했는지 

        binding.searchvm = searchViewModel

        // 아직 초기화되지 않았거나, 아무것도 입력되지 않았을 때 -> 검색 기록
        moveToFragment(SearchHistroyFragment(), "searchhistory")

        // 검색 or 삭제 버튼 클릭 여부
        searchViewModel.searchBTstate.observe(this@SearchFragment, Observer {
            if (it == true) { // 돋보기 일경우 -> 기본 상태
                binding.searchImageviewSearchordelete.setImageResource(R.drawable.icon_search)
            } else { // x버튼 일경우 -> 검색을 엔터를 눌렀다는 것 -> 키보드가 내려감
                binding.searchImageviewSearchordelete.setImageResource(R.drawable.icon_close)
                // 키보드 숨기기
                hidekeyboard(requireActivity())
            }
            binding.searchEditQeury.clearFocus() // 검색이건, 삭제건 버튼을 누르면 edittext의 focus를 없앤다.
        })

        // 검색 or 삭제 버튼 클릭 이벤트
        binding.searchImageviewSearchordelete.setOnClickListener {
            if (binding.searchEditQeury.text.isNullOrEmpty()) { // 에딧텍스트뷰가 비었을 때
            } else { // 글자가 있을 때
                if (searchViewModel.searchBTstate.value == true) { // 돋보기 일경우
                    searchWord()
                    searchViewModel.setSearchBTstate()
                } else { //  x버튼일 경우
                    binding.searchEditQeury.text = null
                    searchViewModel.setSearchBTstate()
                }
            }
        }

        // 키보드 엔터 이벤트
        binding.searchEditQeury.setOnEditorActionListener { textview, actionId, event -> // 키보드 엔터를 눌렀을 경우
            if (binding.searchEditQeury.text.isNullOrEmpty()) { // 에딧텍스트뷰가 비었을 때
            } else { // 글자가 있을 때
                if (searchViewModel.searchBTstate.value == true) { // 돋보기 모양을 눌렀을 경우
                    searchWord()
                    searchViewModel.setSearchBTstate()
                }
            }
            true
        }

        // editText에 입력 단어 감지
        searchViewModel.searchword_data.observe(this, Observer {
            // 한 글자라도 쳤을 때
            if (it != null && it.length > 0) {
                // 검색 결과
                moveToFragment(SearchResultFragment(), "searchresult")
            } else {
                // 아직 초기화되지 않았거나, 아무것도 입력되지 않았을 때 -> 검색 기록
                moveToFragment(SearchHistroyFragment(), "searchhistory")
            }
        })
    }

    // 단어 검색시 검색 기록 남기기, 검색 결과 보여주기
    fun searchWord() {
        // 검색 기록 저장
        searchViewModel.saveSearchHistory(searchViewModel.searchword_data.value.toString())
        // 검색 결과
        moveToFragment(SearchResultFragment(), "searchresult")
    }

    // fragment 이동
    fun moveToFragment(fragment: Fragment, tag: String) {
        childFragmentManager
            .beginTransaction()
            .replace(
                R.id.search_innerlayout_result,
                fragment, // 검색 결과
                tag
            )
            .commitAllowingStateLoss()
    }
}

package com.umc.oppla.base

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.oppla.R

abstract class BaseFragment<T : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : Fragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    // 뒤로가기 버튼을 눌렀을 때를 위한 callback 변수
    lateinit var callback: OnBackPressedCallback

    // 툴바
    lateinit var baseToolbar: Toolbar

    protected open fun savedatainit() {}

    protected abstract fun init()

    protected fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // 하단 바 숨길때
    protected fun hideBottomNavigation(bool: Boolean) {
        if (requireActivity().findViewById<BottomNavigationView>(R.id.main_bottomnavigation_bnb) != null) {
            val bottom: BottomNavigationView =
                requireActivity().findViewById(R.id.main_bottomnavigation_bnb)
            if (bool == true) {
                bottom.visibility = View.GONE
            } else {
                bottom.visibility = View.VISIBLE
            }
        }
    }

    // 뒤로가기 버튼을 눌렀을 때
    open protected fun backpress() {
        // 최상위 Fragment가 아닐 때만 뒤로가기 버튼 활성화
        // -> 최상위 Fragment는 bottomnavigationview에 올라가는 것이기 때문에 뒤로가기 버튼을 활성화 하면
        // Activity의 supportfragmentmanger의 stack에서 popbackstack 하게 된다.( 단, 메뉴 선택 기록을 기록했을 경우 )
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (parentFragmentManager.backStackEntryCount != 0) {
                    parentFragmentManager
                        .popBackStackImmediate(null, 0)
                } else {
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    protected open fun initAppbar(
        toolbar: Toolbar,
        menu: Int?,
        backbutton: Boolean,
        title: String?,
    ) {
        // 툴바, 툴바 검색 기록 레이아웃, 툴바 메뉴 연결
        baseToolbar = toolbar
        baseToolbar.title
        // 툴바 메뉴 추가
        if (menu != null) baseToolbar.inflateMenu(menu)
        // 뒤로가기 버튼
        if (backbutton) {
            baseToolbar.setNavigationIcon(R.drawable.icon_back)
            // 툴바 뒤로가기 버튼 클릭 시 효과
            baseToolbar.setNavigationOnClickListener {
                if (parentFragmentManager.backStackEntryCount > 0) {
                    parentFragmentManager.popBackStackImmediate(null, 0)
                }
            }
        } else {
            baseToolbar.navigationIcon = null
        }

        // 툴바 제목
        if (title != null) {
            baseToolbar.setTitle(title)
        }
    }

    protected open fun initAppbarItem() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        if (savedInstanceState == null) {
            savedatainit()
        }
        backpress()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 프래그먼트가 종료되면 callback 변수 제거
    override fun onDetach() {
        super.onDetach()
        if (::callback.isInitialized) {
            callback.remove()
        }
    }
}
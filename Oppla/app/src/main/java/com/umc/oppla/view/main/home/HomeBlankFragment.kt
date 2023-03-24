package com.umc.oppla.view.main.home

import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentHomeBlankBinding
import com.umc.oppla.view.main.home.map.MapFragment


class HomeBlankFragment : BaseFragment<FragmentHomeBlankBinding>(R.layout.fragment_home_blank) {
    override fun savedatainit() {
        // 초기 화면
        childFragmentManager
            .beginTransaction()
            .replace(R.id.homeblank_layout, MapFragment(), "map")
            .commitAllowingStateLoss()
    }

    override fun init() {

    }
}
package com.umc.oppla.view.main.mypage

import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentMypageBlankBinding
import com.umc.oppla.view.main.mypage.mypagehome.MypageFragment

class MypageBlankFragment : BaseFragment<FragmentMypageBlankBinding>(R.layout.fragment_mypage_blank) {

    override fun savedatainit() {
        // 초기 화면
        childFragmentManager
            .beginTransaction()
            .replace(R.id.mypageblank_layout, MypageFragment(), "mypage")
            .commitAllowingStateLoss()
    }

    override fun init() {

    }

}
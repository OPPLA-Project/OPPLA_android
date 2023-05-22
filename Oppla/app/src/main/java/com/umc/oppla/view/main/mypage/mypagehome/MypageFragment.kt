package com.umc.oppla.view.main.mypage.mypagehome

import android.view.View
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentMypageBinding
import com.umc.oppla.view.main.mypage.annoucement.AnnouncementFragment
import com.umc.oppla.view.main.mypage.history.HistoryFragment
import com.umc.oppla.view.main.mypage.inquiry.InquiryFragment
import com.umc.oppla.view.main.mypage.profile.ProfileFragment
import com.umc.oppla.view.main.mypage.setting.SettingFragment

class MypageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {
    override fun init() {
        binding.apply {
            initAppbar(mypageToolbar.toolbarToolbar, null, false, null)
            mypageToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            mypageToolbar.toolbarTextviewTitle.text = "마이페이지"
        }

        binding.mypageTextviewSetting.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.mypageblank_layout, SettingFragment(), "running")
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding.mypageTextviewProfile.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.mypageblank_layout, ProfileFragment(), "running")
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding.mypageTextviewHistory.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.mypageblank_layout, HistoryFragment(), "running")
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding.mypageTextviewAnnouncement.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.mypageblank_layout, AnnouncementFragment(), "running")
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding.mypageTextviewInquiry.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.mypageblank_layout, InquiryFragment(), "running")
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }

}
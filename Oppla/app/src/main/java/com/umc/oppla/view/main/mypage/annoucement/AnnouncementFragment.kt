package com.umc.oppla.view.main.mypage.annoucement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentAnnouncementBinding

class AnnouncementFragment : BaseFragment<FragmentAnnouncementBinding>(R.layout.fragment_announcement) {
    override fun init() {
        binding.apply {
            initAppbar(announcementToolbar.toolbarToolbar, null, true, null)
            announcementToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            announcementToolbar.toolbarTextviewTitle.text = "공지사항"
        }
    }

}
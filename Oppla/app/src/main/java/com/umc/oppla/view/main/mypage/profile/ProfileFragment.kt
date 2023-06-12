package com.umc.oppla.view.main.mypage.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentProfileBinding


class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    override fun init() {
        binding.apply {
            initAppbar(profileToolbar.toolbarToolbar, null, true, null)
            profileToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            profileToolbar.toolbarTextviewTitle.text = "프로필"
        }
    }

}
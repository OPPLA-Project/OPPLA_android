package com.umc.oppla.view.main.mypage.profile.fix

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentProfileFixBinding

class ProfileFixFragment : BaseFragment<FragmentProfileFixBinding>(R.layout.fragment_profile_fix) {
    override fun init() {
        binding.apply {
            initAppbar(profilefixToolbar.toolbarToolbar, null, true, null)
            profilefixToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            profilefixToolbar.toolbarTextviewTitle.text = "프로필"
        }
    }

}
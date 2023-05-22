package com.umc.oppla.view.main.mypage.setting

import android.view.View
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentSettingBinding

class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    override fun init() {
        binding.apply {
            initAppbar(settingToolbar.toolbarToolbar, null, true, null)
            settingToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            settingToolbar.toolbarTextviewTitle.text = "설정"
        }
    }

}
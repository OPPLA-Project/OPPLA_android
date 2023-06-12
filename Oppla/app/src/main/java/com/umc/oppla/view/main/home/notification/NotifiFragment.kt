package com.umc.oppla.view.main.home.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentNotifiBinding

class NotifiFragment : BaseFragment<FragmentNotifiBinding>(R.layout.fragment_notifi) {
    override fun init() {
        binding.apply {
            initAppbar(notifiToolbar.toolbarToolbar, null, true, null)
            notifiToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            notifiToolbar.toolbarTextviewTitle.text = "알림"
        }
    }

}
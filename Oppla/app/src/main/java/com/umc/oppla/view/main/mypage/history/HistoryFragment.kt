package com.umc.oppla.view.main.mypage.history

import android.view.View
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentHistoryBinding

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history) {
    override fun init() {
        binding.apply {
            initAppbar(histroyToolbar.toolbarToolbar, null, true, null)
            histroyToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            histroyToolbar.toolbarTextviewTitle.text = "거래 내역"
        }
    }

}
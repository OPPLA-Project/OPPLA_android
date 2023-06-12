package com.umc.oppla.view.main.mypage.inquiry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentInquiryBinding

class InquiryFragment : BaseFragment<FragmentInquiryBinding>(R.layout.fragment_inquiry) {
    override fun init() {
        binding.apply {
            initAppbar(inquiryToolbar.toolbarToolbar, null, true, null)
            inquiryToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            inquiryToolbar.toolbarTextviewTitle.text = "문의사항"
        }
    }

}
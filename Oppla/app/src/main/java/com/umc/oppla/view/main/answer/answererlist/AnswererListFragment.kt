package com.umc.oppla.view.main.answer.answererlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentAnswererListBinding


class AnswererListFragment :
    BaseFragment<FragmentAnswererListBinding>(R.layout.fragment_answerer_list) {
    override fun init() {
        binding.apply {
            initAppbar(answererlistToolbar.toolbarToolbar, null, true, null)
            answererlistToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            answererlistToolbar.toolbarTextviewTitle.text = "답변자 목록"
        }
    }

}
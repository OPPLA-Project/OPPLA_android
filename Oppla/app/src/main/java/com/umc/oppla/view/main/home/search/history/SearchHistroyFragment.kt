package com.umc.oppla.view.main.home.search.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentSearchHistroyBinding


class SearchHistroyFragment : BaseFragment<FragmentSearchHistroyBinding>(R.layout.fragment_search_histroy) {
    override fun init() {

    }
    override fun backpress() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (parentFragment!!.parentFragmentManager.backStackEntryCount != 0) {
                    parentFragment!!.parentFragmentManager
                        .popBackStackImmediate(null, 0)
                } else {
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}
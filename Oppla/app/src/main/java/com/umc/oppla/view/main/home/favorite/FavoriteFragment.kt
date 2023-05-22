package com.umc.oppla.view.main.home.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentFavoriteBinding

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(R.layout.fragment_favorite) {
    override fun init() {
        binding.apply {
            initAppbar(favoriteToolbar.toolbarToolbar, null, true, null)
            favoriteToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            favoriteToolbar.toolbarTextviewTitle.text = "즐겨찾기 목록"
        }
    }

}
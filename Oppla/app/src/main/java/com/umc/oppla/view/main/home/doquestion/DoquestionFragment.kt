package com.umc.oppla.view.main.home.doquestion

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.umc.oppla.R
import com.umc.oppla.base.BaseFragment
import com.umc.oppla.databinding.FragmentDoquestionBinding
import com.umc.oppla.view.main.MainActivity
import com.umc.oppla.viewmodel.LocationViewModel

class DoquestionFragment : BaseFragment<FragmentDoquestionBinding>(R.layout.fragment_doquestion) {
    lateinit var locationViewModel: LocationViewModel

    override fun init() {
        locationViewModel = (activity as MainActivity).locationViewModel

        binding.apply {
            initAppbar(doquestionToolbar.toolbarToolbar, null, true, null)
            doquestionToolbar.toolbarTextviewTitle.visibility = View.VISIBLE
            doquestionToolbar.toolbarTextviewTitle.text = "질문하기"

            locationViewModel.doquestionlocation.observe(this@DoquestionFragment, Observer {
                if (it != null) {
                    Log.d("whatisthis", "doquestion location : ${it.documents}")
                    doquestionTextviewLocationname.text = it.documents!![0]?.road_address?.building_name
                    if(it.documents[0]?.road_address!=null) doquestionTextviewLocationaddress.text = it.documents[0]?.road_address?.address_name
                    else  doquestionTextviewLocationaddress.text =it.documents[0]?.address?.address_name
                }
            })
        }

    }
}
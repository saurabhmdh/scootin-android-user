package com.scootin.view.fragment.account.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.scootin.R
import com.scootin.databinding.FragmentMyOrderTrackBinding
import com.scootin.databinding.FragmentMyOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.account.MyOrderCartViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyOrderTrack : Fragment(R.layout.fragment_my_order_track) {

    private var binding by autoCleared<FragmentMyOrderTrackBinding>()
    private val viewModel: MyOrderCartViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyOrderTrackBinding.bind(view)

        updateListeners()
    }

    private fun updateListeners(){


    }

}
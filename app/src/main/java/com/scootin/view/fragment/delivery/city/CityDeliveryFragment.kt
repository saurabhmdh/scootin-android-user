package com.scootin.view.fragment.delivery.city


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentCitywideDeliveryBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.dialogs.CitywideCategoryDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CityDeliveryFragment : Fragment(R.layout.fragment_citywide_delivery) {
    private var binding by autoCleared<FragmentCitywideDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCitywideDeliveryBinding.bind(view)
    }

}
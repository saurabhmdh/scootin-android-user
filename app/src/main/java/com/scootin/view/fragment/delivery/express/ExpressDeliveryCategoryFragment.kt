package com.scootin.view.fragment.delivery.express

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.database.table.Cache
import com.scootin.databinding.FragmentExpressDeliveryBinding
import com.scootin.extensions.getCheckedRadioButtonPosition
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.util.constants.AppConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ExpressDeliveryCategoryFragment : Fragment(R.layout.fragment_express_delivery) {
    private var binding by autoCleared<FragmentExpressDeliveryBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpressDeliveryBinding.bind(view)
        setupListener()
    }

    private fun setupListener() {
        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.btnDone.setOnClickListener {
            val position = binding.radioGroup.getCheckedRadioButtonPosition()
            val selectedView = binding.radioGroup.get(position)

            Timber.i("selected view ${selectedView.tag}")
            viewModel.updateMainCategory(it.tag as String?)

            //Move to next screen
        }
    }


}


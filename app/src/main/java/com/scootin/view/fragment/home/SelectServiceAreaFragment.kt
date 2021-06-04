package com.scootin.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.R
import com.scootin.databinding.FragmentServiceAreaBinding
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.MainActivity
import com.scootin.view.fragment.BaseFragment
import com.scootin.view.vo.ServiceArea
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SelectServiceAreaFragment: BaseFragment(R.layout.fragment_service_area) {

    private var binding by autoCleared<FragmentServiceAreaBinding>()
    private val viewModel: HomeFragmentViewModel by viewModels()

    var serviceAreaInfo: ServiceArea? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentServiceAreaBinding.bind(view)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        setAdapter()
        setupListener()

    }

    private fun setupListener() {
        viewModel.getAllServiceArea().observe(viewLifecycleOwner) {
            if(it.isSuccessful) {
                val list = it.body() ?: emptyList()

                val previous = viewModel.getPreviousSelection() ?: -1
                val selection = list.indexOfFirst { it.id == previous}
                Timber.i("Selection $selection and previous value = $previous")

                val adapter =  ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, list)
                binding.recycler.adapter = adapter
                binding.recycler.setItemChecked(selection, true)
            }
        }

        viewModel.getServiceArea().observe(viewLifecycleOwner) {cache->
            cache?.value?.let {data->
                val listType = object : TypeToken<ServiceArea>() {}.type
                serviceAreaInfo = Gson().fromJson<ServiceArea>(data, listType)
            }
        }

        binding.save.setOnClickListener {
            val position = binding.recycler.checkedItemPosition
            if (position != -1) {
                val data = binding.recycler.getItemAtPosition(position) as ServiceArea?
                data?.let {
                    viewModel.saveServiceArea(it).observe(viewLifecycleOwner) {
                        if(activity is MainActivity) {
                            findNavController().popBackStack()
                        } else {
                            openHomeScreen()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), R.string.service_area_not_selected, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun openHomeScreen() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity?.finish()
    }
    private fun setAdapter() {
        binding.recycler.choiceMode = ListView.CHOICE_MODE_SINGLE
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            if (serviceAreaInfo != null) {
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), R.string.service_area_not_selected, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
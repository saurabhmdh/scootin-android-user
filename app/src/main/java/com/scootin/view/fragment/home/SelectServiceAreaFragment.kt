package com.scootin.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentServiceAreaBinding
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.MainActivity
import com.scootin.view.fragment.BaseFragment
import com.scootin.view.vo.ServiceArea
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectServiceAreaFragment: BaseFragment(R.layout.fragment_service_area) {

    private var binding by autoCleared<FragmentServiceAreaBinding>()
    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentServiceAreaBinding.bind(view)
        setAdapter()
        setupListener()

    }

    private fun setupListener() {
        viewModel.getAllServiceArea().observe(viewLifecycleOwner) {
            if(it.isSuccessful) {
                val list = it.body() ?: emptyList()
                val adapter =  ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, list)
                binding.recycler.adapter = adapter
            }
        }

        binding.save.setOnClickListener {
            val position = binding.recycler.checkedItemPosition
            if (position != -1) {
                val data = binding.recycler.getItemAtPosition(position) as ServiceArea?
                data?.id?.let {
                    viewModel.saveServiceArea(it).observe(viewLifecycleOwner) {
                        openHomeScreen()
                    }
                }
            } else {
                Toast.makeText(requireContext(), R.string.service_area_not_selected, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAdapter() {
        binding.recycler.choiceMode = ListView.CHOICE_MODE_SINGLE
    }

    private fun openHomeScreen() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity?.finish()
    }
}
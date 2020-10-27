package com.scootin.view.fragment.status

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentCartListBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.AddCartItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PaymentStatusFragment : Fragment(R.layout.fragment_paymentt_status) {
    private var binding by autoCleared<FragmentCartListBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var addCartItemAdapter: AddCartItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartListBinding.bind(view)
        setAddCartListAdapter()
        setListener()
    }

    private fun setAddCartListAdapter() {
        addCartItemAdapter = AddCartItemAdapter(
            appExecutors
        )

        binding.productList.apply {
            adapter = addCartItemAdapter
        }
    }

    private fun setListener() {
    }
}
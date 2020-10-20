package com.scootin.view.fragment.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentCartListBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.AddCartItemAdapter
import com.scootin.view.fragment.delivery.essential.EssentialsGroceryDeliveryFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartListFragment : Fragment(R.layout.fragment_cart_list) {
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
        binding.confirmButton.setOnClickListener {
            findNavController().navigate(CartListFragmentDirections.addCartToWallet())
        }
    }
}
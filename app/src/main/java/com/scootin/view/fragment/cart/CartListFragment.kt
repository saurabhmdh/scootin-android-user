package com.scootin.view.fragment.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentCartListBinding
import com.scootin.network.AppExecutors
import com.scootin.network.manager.AppHeaders
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.AddCartItemAdapter
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CartListFragment : Fragment(R.layout.fragment_cart_list) {
    private var binding by autoCleared<FragmentCartListBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var addCartItemAdapter: AddCartItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartListBinding.bind(view)
        setAddCartListAdapter()
        setData()
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
        binding.checkout.setOnClickListener {
            findNavController().navigate(CartListFragmentDirections.actionCartFragmentToCardPayment())
        }
    }

    fun setData() {
        Timber.i("userId = ${AppHeaders.userID}")
        viewModel.userCartList()
        viewModel.getUserCartListLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful) {
                Timber.i("userCartList = ${it.body()?.size}")
                val data = it.body()
                addCartItemAdapter.submitList(data)
            }
        })
    }


}
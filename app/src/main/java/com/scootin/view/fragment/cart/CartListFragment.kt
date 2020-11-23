package com.scootin.view.fragment.cart

import android.os.BaseBundle
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.bindings.setPrice
import com.scootin.databinding.FragmentCartListBinding
import com.scootin.extensions.orDefault
import com.scootin.extensions.orZero
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.PlaceOrderRequest
import com.scootin.util.constants.AppConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.MainActivity
import com.scootin.view.adapter.AddCartItemAdapter
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.cart.CartViewModel
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CartListFragment : BaseFragment(R.layout.fragment_cart_list) {
    private var binding by autoCleared<FragmentCartListBinding>()
    private val viewModel: CartViewModel by viewModels()

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
            showLoading()
            viewModel.generateOrder(AppHeaders.userID, PlaceOrderRequest(AppConstants.defaultAddressId, false)).observe(viewLifecycleOwner) {

                when (it.status) {
                    Status.SUCCESS -> {
                        dismissLoading()
                        findNavController().navigate(CartListFragmentDirections.actionCartFragmentToCardPayment(it.data?.id.orZero()))
                    }
                    Status.LOADING -> {}
                    Status.ERROR -> {
                        dismissLoading()
                        Toast.makeText(context, "Unable to place order", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setData() {
        Timber.i("userId = ${AppHeaders.userID}")
        viewModel.userCartList()
        viewModel.getUserCartListLiveData.observe(viewLifecycleOwner, {
            if (it.isSuccessful) {
                Timber.i("userCartList = ${it.body()?.size}")
                val data = it.body()
                enableDisableVisibility(data.isNullOrEmpty())
                addCartItemAdapter.submitList(data)
            }
        })

        viewModel.getTotalPrice(AppHeaders.userID).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.price.setPrice(it.data.orDefault(0.0))
                }
                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }

        binding.shopNow.setOnClickListener {
            val activity = activity as MainActivity?
            activity?.moveToAnotherTab(R.id.home)
        }

        viewModel.getCartCount(AppHeaders.userID).observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val result = it.body()?.toInt()
                val activity = activity as MainActivity?
                activity?.setupBadging(result.orZero())
            }
        }
    }

    private fun enableDisableVisibility(empty: Boolean) {
        if (empty) {
            binding.emptyText.visibility = View.VISIBLE
            binding.shopNow.visibility = View.VISIBLE

            binding.productList.visibility = View.GONE
            binding.checkoutLayout.visibility = View.GONE
        } else {
            binding.emptyText.visibility = View.GONE
            binding.shopNow.visibility = View.GONE

            binding.productList.visibility = View.VISIBLE
            binding.checkoutLayout.visibility = View.VISIBLE
        }
    }


}
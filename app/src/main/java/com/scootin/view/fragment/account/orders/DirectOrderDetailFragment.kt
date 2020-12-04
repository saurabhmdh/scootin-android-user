package com.scootin.view.fragment.account.orders

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.razorpay.Checkout
import com.scootin.R
import com.scootin.databinding.FragmentMyOrderTrackBinding
import com.scootin.databinding.FragmentTrackDirectOrderBinding
import com.scootin.extensions.orDefault
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.response.orderdetail.OrderDetail
import com.scootin.util.Conversions
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.ExtraDataAdapter
import com.scootin.viewmodel.account.OrderFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DirectOrderDetailFragment : Fragment(R.layout.fragment_track_direct_order) {

    private var binding by autoCleared<FragmentTrackDirectOrderBinding>()
//    private val viewModel: MyOrderCartViewModel by viewModels()

    private val viewModel: OrderFragmentViewModel by viewModels()
    private var itemsAdapter by autoCleared<ExtraDataAdapter>()
    private val args: DirectOrderDetailFragmentArgs by navArgs()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrackDirectOrderBinding.bind(view)
        binding.back.setOnClickListener { findNavController().popBackStack() }
        updateViewModel()
        updateListeners()
        initUI()
    }

    private fun updateViewModel() {
        viewModel.getDirectOrder(args.orderId).observe(viewLifecycleOwner, Observer {
            Timber.i("orderId = ${it.status} : ${it.data}")
            when (it.status) {
                Status.SUCCESS -> {
                    binding.data = it.data
                    updateSelectors(it.data?.orderStatus)
                    if (it.data?.extraData.isNullOrEmpty().not()) {
                        val extra = Conversions.convertExtraData(it.data?.extraData)
                        Timber.i("Extra $extra")
                        itemsAdapter.submitList(extra)
                    }
                    if (it.data?.media == null) {
                        binding.orderList.visibility = View.GONE
                    }

                    //payOnDeliveryHeader
                }
            }
        })
    }



    private fun updateListeners() {

        binding.helpKey.setOnClickListener {
            findNavController().navigate(DirectOrderDetailFragmentDirections.orderToCustomerSupport())
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }

    }

    private fun updateSelectors(orderStatus: String?) {
        orderStatus?.let {
            when(it) {
                "PLACED" -> {
                    binding.placeIcon.isSelected = true
                    binding.orderStatusString.text = getString(R.string.order_has_been_placed)
                }
                "PACKED" -> {
                    binding.placeIcon.isSelected = true
                    binding.progressId.isSelected=true
                    binding.packedIcon.isSelected = true
                    binding.orderStatusString.text = getString(R.string.order_has_been_packed)
                }
                "DISPATCHED" -> {
                    binding.placeIcon.isSelected = true
                    binding.progressId.isSelected=true
                    binding.packedIcon.isSelected = true
                    binding.progressId2.isSelected=true
                    binding.dispatchedIcon.isSelected = true
                    binding.orderStatusString.text = getString(R.string.order_has_been_dispatched)
                }
                "COMPLETED" -> {
                    binding.placeIcon.isSelected = true
                    binding.progressId.isSelected=true
                    binding.packedIcon.isSelected = true
                    binding.progressId2.isSelected=true
                    binding.dispatchedIcon.isSelected = true
                    binding.progressId3.isSelected=true
                    binding.deliveredIcon.isSelected = true
                    binding.orderStatusString.text = getString(R.string.order_has_been_completed)
                }
            }
        }

    }
    private fun initUI() {
        itemsAdapter = ExtraDataAdapter(appExecutors)
        binding.recycler.adapter=itemsAdapter
    }
}
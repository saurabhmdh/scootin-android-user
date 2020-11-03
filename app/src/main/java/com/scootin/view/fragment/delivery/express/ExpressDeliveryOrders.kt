package com.scootin.view.fragment.delivery.express

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.scootin.R
import com.scootin.databinding.FragmentExpressDeliveryOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.glide.GlideApp
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.DirectOrderRequest
import com.scootin.network.response.SearchISuggestiontem
import com.scootin.util.constants.AppConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.util.ui.MediaPicker
import com.scootin.util.ui.UtilPermission
import com.scootin.view.adapter.order.SearchitemAdapter
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.order.DirectOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class ExpressDeliveryOrders : BaseFragment(R.layout.fragment_express_delivery_orders) {
    lateinit var searchItemAddAdapter: SearchitemAdapter
    private var binding by autoCleared<FragmentExpressDeliveryOrdersBinding>()
    val filesCantBeUploadedList = mutableListOf<String>()
    private val viewModel: DirectOrderViewModel by viewModels()
    private var mediaId = -1L
    private val shopId by lazy {
        args.shopId
    }

    private val args: ExpressDeliveryOrdersArgs by navArgs()

    @Inject
    lateinit var appExecutors: AppExecutors
    val itemAddList = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpressDeliveryOrdersBinding.bind(view)
        initListener()
        setSearchSuggestionList()
    }

    private fun initListener() {
        binding.searchSuggestion.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            Timber.i("action id = ${actionId}")
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    Timber.i("action id = ${actionId}")
                    searchItemAddAdapter.addList(
                        SearchISuggestiontem(
                            binding.searchSuggestion.text.toString(),
                            0
                        )
                    )
                    return@OnEditorActionListener true
                }
            }
            false
        })

        binding.placeOrder.setOnClickListener {
            placeDirectOrder()
        }

        binding.appCompatImageView6.setOnClickListener {
            onClickOfUploadMedia()
        }
    }

    private fun setSearchSuggestionList() {
        searchItemAddAdapter =
            SearchitemAdapter(appExecutors, object : SearchitemAdapter.OnItemClickListener {
                override fun onIncrement(count: String) {
                    Timber.i("increment count = $count")
                }

                override fun onDecrement(count: String) {
                    Timber.i("decrement count = $count")
                }

            })

        binding.searchList.apply {
            adapter = searchItemAddAdapter
        }

        binding.mobileNo.setText("Mobile number " + AppHeaders.userMobileNumber)
        binding.storeName.text = "${args.shopName}"
    }

    fun showMediaGallery() {
        if (UtilPermission.hasReadWritePermission(requireActivity())) {
            MediaPicker(requireActivity()).getImagePickerSelectionPanel()
        } else {
            UtilPermission.requestForReadWritePermission(requireActivity())
        }
    }

    private fun placeDirectOrder() {
        if (mediaId == -1L) {
            Toast.makeText(context, "Invalid Media", Toast.LENGTH_SHORT).show()
            return
        }
        showLoading()
        Timber.i("Json : ${Gson().toJson(searchItemAddAdapter.list)}")
        viewModel.placeDirectOrder(
            AppHeaders.userID,
            DirectOrderRequest(
                AppConstants.defaultAddressId,
                true,
                mediaId,
                shopId,
                Gson().toJson(searchItemAddAdapter.list).toString()
            )
        ).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    Toast.makeText(
                        context,
                        "Your order has been received successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(ExpressDeliveryOrdersDirections.directOrderConfirmation())
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    dismissLoading()
                }
            }
        }
    }

    private fun onClickOfUploadMedia() {
        if (UtilPermission.hasReadWritePermission(requireContext())) {
            MediaPicker(requireActivity()).getImagePickerSelectionPanel()
        } else {
            UtilPermission.requestForReadWritePermission(requireActivity())
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == AppConstants.RESULT_LOAD_IMAGE_VIDEO && resultCode == Activity.RESULT_OK && null != intent) {
            //Call view model to upload media..
            showLoading()
            intent.data?.let { uri ->
                viewModel.uploadMedia(uri).observe(viewLifecycleOwner) { response ->
                    dismissLoading()
                    if (response.isSuccessful) {
                        val media = response.body() ?: return@observe
                        GlideApp.with(requireContext()).load(media.url)
                            .into(binding.appCompatImageView7)
                        mediaId = media.id
                    } else {
                        Toast.makeText(context, "There is some error media", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}
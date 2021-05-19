package com.scootin.view.fragment.delivery.express

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.R
import com.scootin.databinding.FragmentExpressDeliveryOrdersBinding
import com.scootin.extensions.getNavigationResult
import com.scootin.extensions.orZero
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.glide.GlideApp
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.DirectOrderRequest
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.ExtraDataItem
import com.scootin.network.response.Media
import com.scootin.util.UtilUIComponent
import com.scootin.util.constants.IntentConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.order.SearchitemAdapter
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.order.DirectOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class ExpressDeliveryOrders : BaseFragment(R.layout.fragment_express_delivery_orders) {
    private var searchItemAddAdapter by autoCleared<SearchitemAdapter>()
    private var binding by autoCleared<FragmentExpressDeliveryOrdersBinding>()

    private val viewModel: DirectOrderViewModel by viewModels()

    private var media: Media? = null

    private val shopId by lazy {
        args.shopId
    }

    private val args: ExpressDeliveryOrdersArgs by navArgs()

    @Inject
    lateinit var appExecutors: AppExecutors

    var address: AddressDetails? = null

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
                    if (binding.searchSuggestion.text.isNullOrEmpty()) {
                        return@OnEditorActionListener false
                    }

                    val count = binding.searchList.adapter?.itemCount.orZero()
                    Timber.i("count = $count")
                    if (count > 10) {
                        Toast.makeText(requireContext(), "Max 10 items supported", Toast.LENGTH_SHORT).show()
                        return@OnEditorActionListener false
                    }
                    searchItemAddAdapter.addList(
                        ExtraDataItem(
                            binding.searchSuggestion.text.toString(),
                            1
                        )
                    )

                    binding.searchList.scrollToPosition(count - 1)
                    binding.searchSuggestion.setText("")
                    return@OnEditorActionListener false
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

        binding.back.setOnClickListener {
            findNavController().popBackStack(R.id.titleScreen, false)
        }

        //Lets load all address if there is no address then ask to add, incase there is
        viewModel.loadAllAddress().observe(viewLifecycleOwner) {
            //find defaultAddress..
            if (it.isSuccessful) {
                if (address != null) {
                    Timber.i("We have address from previous fragment $address")
                    return@observe
                }
                address = it.body()?.first { it.hasDefault }
                Timber.i("We found address ${address}")
                address?.let {
                    binding.dropAddress.text = UtilUIComponent.setOneLineAddress(address)
                }

            } else {
                //We need to
            }
        }
        binding.dropAddress.setOnClickListener {
            viewModel.list = searchItemAddAdapter.list
            viewModel.media = media
            findNavController().navigate(IntentConstants.openAddressPage())
        }
        getNavigationResult()?.observe(viewLifecycleOwner) {
            updateAddressData(it)
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
            layoutManager = LinearLayoutManager(requireContext()).apply {
                reverseLayout = true
            }
            adapter = searchItemAddAdapter
        }

        if (viewModel.list.isEmpty().not()) {
            searchItemAddAdapter.addAdd(viewModel.list)
        }

        binding.mobileNo.setText("Mobile number " + AppHeaders.userMobileNumber)
        binding.storeName.text = "${args.shopName}"
    }

    private fun placeDirectOrder() {
        if (media == null && searchItemAddAdapter.list.isEmpty()) {
            Toast.makeText(context, "Please enter either name or photo", Toast.LENGTH_SHORT).show()
            return
        }
        if (address == null) {
            Toast.makeText(requireContext(), "Please add a address", Toast.LENGTH_LONG).show()
            return
        }
        val mediaId = media?.id ?: -1
        var orderId: Long = -1

        showLoading()
        Timber.i("Json : ${Gson().toJson(searchItemAddAdapter.list)}")
        viewModel.placeDirectOrder(
            AppHeaders.userID,
            DirectOrderRequest(
                address!!.id,
                true,
                AppHeaders.serviceAreaId,
                mediaId,
                shopId,
                Gson().toJson(searchItemAddAdapter.list).toString()
            )
        ).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("order id $orderId")
                    orderId = (it.data?.id ?: -1).toLong()
                    dismissLoading()
                    Toast.makeText(
                        context,
                        "Your order has been received successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(ExpressDeliveryOrdersDirections.expressdeliveryToOrderConfirmation(orderId))
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    dismissLoading()
                }
            }
        }
    }

    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        if (resultCode == Activity.RESULT_OK) {
            data?.data?.path?.let {fileUri->
                uploadMedia(File(fileUri))
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }
    private fun onClickOfUploadMedia() {
        ImagePicker.with(this)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private fun uploadMedia(file: File?) {
        showLoading()
        file?.let {
            viewModel.uploadMedia(it).observe(viewLifecycleOwner) {response->
                Timber.i("Media viewModel.uploadMedia ${response.isSuccessful}")
                if(response.isSuccessful) {
                    dismissLoading()
                    val media = response.body() ?: return@observe
                    this.media = media
                    loadMedia()
                } else {
                    Toast.makeText(context, "There is some error media", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateAddressData(calendarData: String) {
        val result =
            Gson().fromJson<AddressDetails>(calendarData, object : TypeToken<AddressDetails>() {}.type)
                ?: return
        address = result
        binding.dropAddress.text = UtilUIComponent.setOneLineAddress(address)
        this.media = viewModel.media
        loadMedia()
        Timber.i("update the address $result")
    }

    private fun loadMedia() {
        if(media?.url != null) {
            GlideApp.with(requireContext()).load(media?.url).into(binding.mediaImage)
        }
    }
}
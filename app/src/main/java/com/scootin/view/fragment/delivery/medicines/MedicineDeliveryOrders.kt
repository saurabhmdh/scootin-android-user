package com.scootin.view.fragment.delivery.medicines

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.scootin.R
import com.scootin.databinding.FragmentExpressDeliveryOrdersBinding
import com.scootin.databinding.MedicinePrescriptionFragmentBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.glide.GlideApp
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.DirectOrderRequest
import com.scootin.network.response.ExtraDataItem
import com.scootin.util.constants.AppConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.util.ui.MediaPicker
import com.scootin.util.ui.UtilPermission
import com.scootin.view.adapter.order.SearchitemAdapter
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.order.DirectOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.text.FieldPosition
import javax.inject.Inject


@AndroidEntryPoint
class MedicineDeliveryOrders : BaseFragment(R.layout.medicine_prescription_fragment) {
    lateinit var searchItemAddAdapter: SearchitemAdapter
    private var binding by autoCleared<MedicinePrescriptionFragmentBinding>()

    private val viewModel: DirectOrderViewModel by viewModels()
    private var mediaId = -1L


    private val shopId by lazy {
        args.shopId
    }

    private val args: MedicineDeliveryOrdersArgs by navArgs()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MedicinePrescriptionFragmentBinding.bind(view)
        initListener()
        setSearchSuggestionList()
    }

    private fun initListener() {
        binding.searchSuggestion.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            Timber.i("action id = ${actionId}")
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    Timber.i("action id = ${actionId}")

                    //Need to play here
                    searchItemAddAdapter.addList(ExtraDataItem(binding.searchSuggestion.text.toString(), 1))
                    binding.searchSuggestion.setText("")
                    return@OnEditorActionListener false
                }
            }
            false
        })

        binding.placeOrder.setOnClickListener {
            if (binding.termAccepted.isChecked.not()) {
                Toast.makeText(requireContext(), "Please accept term & condition", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            placeDirectOrder()
        }

        binding.uploadPhoto.setOnClickListener {
            onClickOfUploadMedia()
        }
        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.warning.setOnClickListener {
            binding.termAccepted.isChecked = !binding.termAccepted.isChecked
        }
    }

    private fun setSearchSuggestionList() {
        searchItemAddAdapter = SearchitemAdapter(appExecutors, object : SearchitemAdapter.OnItemClickListener {
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

        binding.medicalStoreName.text = "${args.shopName}"
    }

    private fun placeDirectOrder() {
        if (mediaId == -1L) {
            //TODO: add fake media ->
            mediaId = 329
        }

        if (searchItemAddAdapter.list.isEmpty()) {
            Toast.makeText(context, "Please add medicine name", Toast.LENGTH_SHORT).show()
            return
        }
        showLoading()
        Timber.i("Json : ${Gson().toJson(searchItemAddAdapter.list)}")
        viewModel.placeDirectOrder(
            AppHeaders.userID,
            DirectOrderRequest(
                AppConstants.defaultAddressId,
                false,
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
                    findNavController().navigate(MedicineDeliveryOrdersDirections.directOrderConfirmation())
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
        ImagePicker.with(this)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start { resultCode, data ->
                if (resultCode == Activity.RESULT_OK) {
                    uploadMedia(ImagePicker.getFile(data))
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
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
                    GlideApp.with(requireContext()).load(media.url).into(binding.receiverPhotoBox)
                    mediaId = media.id
                } else {
                    Toast.makeText(context, "There is some error media", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
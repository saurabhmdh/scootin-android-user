package com.scootin.view.fragment.delivery.city


import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.R
import com.scootin.databinding.FragmentCitywideDeliveryBinding
import com.scootin.extensions.getNavigationResult
import com.scootin.network.glide.GlideApp
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.DistanceMeasure
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.Media
import com.scootin.util.UtilUIComponent
import com.scootin.util.constants.AppConstants
import com.scootin.util.constants.IntentConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.order.DirectOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class CityDeliveryFragment : BaseFragment(R.layout.fragment_citywide_delivery) {
    private var binding by autoCleared<FragmentCitywideDeliveryBinding>()
    private val viewModel: DirectOrderViewModel by viewModels()

    private var media: Media? = null

    var pickupAddress: AddressDetails? = null
    var dropAddress: AddressDetails? = null

    private var click = 0 //Pick up address if its 1 means drop

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCitywideDeliveryBinding.bind(view)

        updateListener()
        binding.uploadPhoto.setOnClickListener {
            onClickOfUploadMedia()
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.placeOrder.setOnClickListener {
            placeCityWideOrder()
        }
    }

    private fun placeCityWideOrder() {
        //First we need to check accept the term and conditions
        if (binding.termAccepted.isChecked.not()) {
            Toast.makeText(requireContext(), "Please accept term & condition", Toast.LENGTH_SHORT).show()
            return
        }

        if (media == null) {
            Toast.makeText(context, "Invalid Media", Toast.LENGTH_SHORT).show()
            return
        }

        if (pickupAddress == null) {
            Toast.makeText(context, "Pickup address is not valid", Toast.LENGTH_SHORT).show()
            return
        }

        if (dropAddress == null) {
            Toast.makeText(context, "Drop address is not valid", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading()
        viewModel.placeCityWideOrder(
            AppHeaders.userID,
            dropAddress!!.id,
            pickupAddress!!.id,
            media!!.id
        ).observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                dismissLoading()
                Toast.makeText(context, "Your order has been received successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(CityDeliveryFragmentDirections.citydeliveryToSuccess())
            } else {
                dismissLoading()
                Toast.makeText(context, it.errorBody()?.string(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateListener() {
        binding.mobileNo.setText("Mobile number "+ AppHeaders.userMobileNumber)

        binding.warning.setOnClickListener {
            binding.termAccepted.isChecked = !binding.termAccepted.isChecked
        }

        binding.pickupAddress.setOnClickListener {
            click = 0
            viewModel.media = media
            viewModel.dropAddress = dropAddress
            findNavController().navigate(IntentConstants.openAddressPage())
        }

        binding.dropAddress.setOnClickListener {
            click = 1
            viewModel.media = media
            viewModel.pickupAddress = pickupAddress
            findNavController().navigate(IntentConstants.openAddressPage())
        }

        getNavigationResult()?.observe(viewLifecycleOwner) {
            updateAddressData(it, click)
        }

        if (pickupAddress != null && dropAddress != null) {
            viewModel.findDistance(DistanceMeasure(pickupAddress!!.id, dropAddress!!.id)).observe(viewLifecycleOwner) {
                if (it.isSuccessful) {
                    binding.totalDistance.text = "Distance: " + it.body()?.elements?.firstOrNull()?.distance?.text
                }
            }
        }
    }
    private fun updateAddressData(calendarData: String, click: Int) {
        val result =
            Gson().fromJson<AddressDetails>(calendarData, object : TypeToken<AddressDetails>() {}.type)
                ?: return

        when(click) {
            0 -> {
                pickupAddress = result
                dropAddress = viewModel.dropAddress
                if (dropAddress != null) {
                    binding.dropAddress.text = UtilUIComponent.setOneLineAddress(dropAddress)
                }
                binding.pickupAddress.text = UtilUIComponent.setOneLineAddress(pickupAddress)
            }
            1-> {
                dropAddress = result
                pickupAddress = viewModel.pickupAddress
                if (pickupAddress != null) {
                    binding.pickupAddress.text = UtilUIComponent.setOneLineAddress(pickupAddress)
                }
                binding.dropAddress.text = UtilUIComponent.setOneLineAddress(dropAddress)
            }
        }
        this.media = viewModel.media
        loadMedia()
        Timber.i("update the address $result")
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
                    this.media = media
                    loadMedia()
                } else {
                    Toast.makeText(context, "There is some error media", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadMedia() {
        if (media?.url != null) {
            GlideApp.with(requireContext()).load(media?.url).into(binding.receiverPhotoBox)
        }
    }
}


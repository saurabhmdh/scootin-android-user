package com.scootin.view.fragment.delivery.essential

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.imagepicker.ImagePicker
import com.scootin.R
import com.scootin.databinding.HandWrittenGroceryListBinding
import com.scootin.network.api.Status
import com.scootin.network.glide.GlideApp
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.DirectOrderRequest
import com.scootin.util.constants.AppConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.util.ui.MediaPicker
import com.scootin.util.ui.UtilPermission
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.order.DirectOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File


@AndroidEntryPoint
class EssentialHandwrittenFragment : BaseFragment(R.layout.hand_written_grocery_list) {
    private var binding by autoCleared<HandWrittenGroceryListBinding>()
    private val viewModel: DirectOrderViewModel by viewModels()

    private val args: EssentialHandwrittenFragmentArgs by navArgs()

    private val shopId by lazy {
        args.shopId
    }

    private var mediaId = -1L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HandWrittenGroceryListBinding.bind(view)

        updateListener()
        binding.uploadPhoto.setOnClickListener {
            onClickOfUploadMedia()
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.placeOrder.setOnClickListener {
            placeDirectOrder()
        }
    }

    private fun placeDirectOrder() {
        if (mediaId == -1L) {
            Toast.makeText(context, "Invalid Media", Toast.LENGTH_SHORT).show()
            return
        }
        showLoading()
        viewModel.placeDirectOrder(
            AppHeaders.userID,
            DirectOrderRequest(AppConstants.defaultAddressId, false, mediaId, shopId)).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    Toast.makeText(context, "Your order has been received successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(EssentialHandwrittenFragmentDirections.directOrderConfirmation())
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    dismissLoading()
                }
            }
        }
    }

    private fun updateListener() {
        binding.mobileNo.setText("Mobile number "+AppHeaders.userMobileNumber)

    }

    private fun onClickOfUploadMedia() {
        ImagePicker.with(this)
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
        file?.let {
            viewModel.uploadMedia(it).observe(viewLifecycleOwner) {response->
                Timber.i("Media viewModel.uploadMedia ${response.isSuccessful}")
                dismissLoading()
                if(response.isSuccessful) {
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
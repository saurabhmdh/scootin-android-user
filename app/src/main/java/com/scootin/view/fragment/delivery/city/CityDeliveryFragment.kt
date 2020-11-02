package com.scootin.view.fragment.delivery.city


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentCitywideDeliveryBinding
import com.scootin.databinding.HandWrittenGroceryListBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.glide.GlideApp
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.DirectOrderRequest
import com.scootin.util.constants.AppConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.BaseFragment
import com.scootin.util.ui.MediaPicker
import com.scootin.util.ui.UtilPermission
import com.scootin.view.fragment.delivery.essential.EssentialHandwrittenFragmentArgs
import com.scootin.view.fragment.dialogs.CitywideCategoryDialogFragment
import com.scootin.viewmodel.order.DirectOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CityDeliveryFragment : BaseFragment(R.layout.fragment_citywide_delivery) {
    private var binding by autoCleared<FragmentCitywideDeliveryBinding>()
    private val viewModel: DirectOrderViewModel by viewModels()

    private val args: EssentialHandwrittenFragmentArgs by navArgs()

    private val shopId by lazy {
        args.shopId
    }

    private var mediaId = -1L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCitywideDeliveryBinding.bind(view)

        updateListener()
        binding.uploadPhoto.setOnClickListener {
            onClickOfUploadMedia()
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.placeOrder.setOnClickListener {
            //placeDirectOrder()
        }
    }

//    private fun placeDirectOrder() {
//        if (mediaId == -1L) {
//            Toast.makeText(context, "Invalid Media", Toast.LENGTH_SHORT).show()
//            return
//        }
//        showLoading()
//        viewModel.placeDirectOrder(
//            AppHeaders.userID,
//            DirectOrderRequest(AppConstants.defaultAddressId, false, mediaId, shopId)
//        ).observe(viewLifecycleOwner) {
//            when(it.status) {
//                Status.SUCCESS -> {
//                    dismissLoading()
//                    Toast.makeText(context, "Your order has been received successfully", Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(CityDeliveryFragmentDirections.directOrderConfirmation())
//                }
//                Status.LOADING -> {}
//                Status.ERROR -> {
//                   dismissLoading()
//                }
//            }
//        }
//    }

    private fun updateListener() {
        binding.mobileNo.setText("Mobile number "+ AppHeaders.userMobileNumber)

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
                viewModel.uploadMedia(uri).observe(viewLifecycleOwner) {response->
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
    }


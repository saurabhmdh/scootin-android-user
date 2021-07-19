package com.scootin.view.fragment.delivery.essential

import android.app.Activity
import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.R
import com.scootin.databinding.HandWrittenGroceryListBinding
import com.scootin.extensions.getNavigationResult
import com.scootin.network.api.Status
import com.scootin.network.glide.GlideApp
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.DirectOrderRequest
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.Media
import com.scootin.util.UtilUIComponent
import com.scootin.util.constants.IntentConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.BaseFragment
import com.scootin.viewmodel.order.DirectOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.hand_written_grocery_list.*
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
    private val shopName by lazy {
        args.shopName
    }
//    private val deliverySlot by lazy {
//        args.deliverySlot
//    }

    var orderId: Long = -1

    private var media: Media? = null

    var address: AddressDetails? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HandWrittenGroceryListBinding.bind(view)
        binding.storeName.setText(shopName)
        updateListener()
        binding.uploadPhoto.setOnClickListener {
            onClickOfUploadMedia()
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.placeOrder.setOnClickListener {
            placeDirectOrder()
        }
        binding.address.setOnClickListener {
            viewModel.media = media
            findNavController().navigate(IntentConstants.openAddressPage())
        }
        getNavigationResult()?.observe(viewLifecycleOwner) {
            updateAddressData(it)
        }
    }

    private fun placeDirectOrder() {
        if (media == null) {
            Toast.makeText(context, "Invalid Media", Toast.LENGTH_SHORT).show()
            return
        }
        if (address == null) {
            Toast.makeText(context, "Address is not valid", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading()
        viewModel.placeDirectOrder(

            AppHeaders.userID,
            DirectOrderRequest(address!!.id, false, AppHeaders.serviceAreaId, media!!.id, shopId)).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {

                    Timber.i("order id $orderId")
                    dismissLoading()
                    orderId = (it.data?.id ?: -1).toLong()

                    Toast.makeText(context, "Your order has been received successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(EssentialHandwrittenFragmentDirections.directOrderConfirmation(orderId))
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
    private fun updateAddressData(calendarData: String) {
        val result =
            Gson().fromJson<AddressDetails>(calendarData, object : TypeToken<AddressDetails>() {}.type)
                ?: return
        address = result
        binding.address.text = UtilUIComponent.setOneLineAddress(address)
        this.media = viewModel.media
        loadMedia()
        Timber.i("update the address $result")
    }

    private fun onClickOfUploadMedia() {
        ImagePicker.with(this)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        if (resultCode == Activity.RESULT_OK) {
            if (data?.data?.scheme == "file") {
                data.data?.path?.let {fileUri->
                    uploadMedia(File(fileUri))
                }
            } /*else if(data?.data?.scheme == "content") {
                data.data?.let {
                    val fileUri = getCursorContent(it)
                    if(fileUri != null) {
                        uploadMedia(fileUri)
                    }
                }
            }*/
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }
    private fun getCursorContent(selectedImage: Uri): File? =
        kotlin.runCatching {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
             val cursor = requireContext().contentResolver.query(
                    selectedImage,
                    filePathColumn, null, null, null
                )

                if (cursor != null) {
                    cursor.moveToFirst()
                    val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath: String = cursor.getString(columnIndex)
                    cursor.close()
                    File(picturePath)
                }
            null

        }.getOrNull()

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
        if(media?.url != null) {
            GlideApp.with(requireContext()).load(media?.url).into(binding.receiverPhotoBox)
        }
    }

}
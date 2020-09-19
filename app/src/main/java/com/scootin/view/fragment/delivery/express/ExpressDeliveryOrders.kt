package com.scootin.view.fragment.delivery.express

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.scootin.R
import com.scootin.databinding.FragmentExpressDeliveryBinding
import com.scootin.databinding.FragmentExpressDeliveryOrdersBinding
import com.scootin.databinding.SelectedImageVideoBinding
import com.scootin.network.AppExecutors
import com.scootin.util.constants.AppConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.util.interfaces.IMediaItem
import com.scootin.util.media.UploadedItem
import com.scootin.util.ui.MediaPicker
import com.scootin.util.ui.UtilPermission
import com.scootin.util.ui.presentSnackBar
import com.scootin.view.adapter.EDItemAddAdapter
import com.scootin.view.adapter.UploadImageAdapter
import com.scootin.view.fragment.dialogs.CategoryDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ExpressDeliveryOrders : Fragment(R.layout.fragment_express_delivery_orders) {
    private var binding by autoCleared<FragmentExpressDeliveryOrdersBinding>()
    val filesCantBeUploadedList = mutableListOf<String>()
    private var snackbar: Snackbar? = null

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var mediaAdapter: UploadImageAdapter
    private lateinit var edItemAddAdapter: EDItemAddAdapter
    private lateinit var items: HashMap<String, IMediaItem>
    val itemAddList = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpressDeliveryOrdersBinding.bind(view)

        val dailog = CategoryDialogFragment()
        dailog.show(childFragmentManager, "")

//        setAdaper()
//        binding.save.setOnClickListener {
//            if(binding.itemAddEditText.text.toString().isNotEmpty()) {
//                itemAddList.add(binding.itemAddEditText.text.toString())
//                edItemAddAdapter.submitList(itemAddList)
//                binding.itemAddEditText.setText("")
//            }
//        }
//        binding.uploadPhoto.setOnClickListener {
//            showMediaGallery()
//        }

    }

    private fun setAdaper() {
        edItemAddAdapter =
            EDItemAddAdapter(appExecutors, object : EDItemAddAdapter.ImageAdapterClickLister {
                override fun onIncrementItem(view: View) {
                }

                override fun onDecrementItem(view: View) {
                }

            })

//        binding.list.apply {
//            adapter = edItemAddAdapter
//        }

        mediaAdapter =
            UploadImageAdapter(
                appExecutors,
                object : UploadImageAdapter.ImageAdapterClickLister {
                    override fun onImportingImage(
                        binding: SelectedImageVideoBinding,
                        item: UploadedItem
                    ) {
                    }

                    override fun onDiscardButtonClick(item: UploadedItem) {
                    }

                    override fun onUploadMoreButtonClick(view: View) {
                    }

                })
//        binding.selectedImageGallery.apply {
//            adapter = mediaAdapter
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AppConstants.RESULT_LOAD_IMAGE_VIDEO && resultCode == Activity.RESULT_OK && null != data) {
            context?.let { context ->

                // remove all the item from the array, start with fresh
                filesCantBeUploadedList.removeAll(filesCantBeUploadedList)

                // Present message to user if user is trying to uploading more than 9 images
                // If only one image/video is selected,  it will not be in ClipData
                data.clipData?.apply {
                    for (i in 0 until itemCount) {
                        // present message to user message if user is imported more than 9 media life in one pick
                        val imageUri: Uri = getItemAt(i).uri
                        if (isMediaHasValidSize(context, imageUri))
                            setSelectedImageToAdapter(imageUri)
                    }
                }

                // if only single image / video selected
                if (data.clipData == null && data.data != null) {
                    val uri = MediaPicker(requireActivity()).getImageUrl(context, data)
                    uri?.let { it ->
                        if (isMediaHasValidSize(context, it))
                            setSelectedImageToAdapter(it)
                    }
                }

                // Present message to the user for those files which can't be uploaded
                if (filesCantBeUploadedList.isNotEmpty()) {
                    val listOfFiles =
                        filesCantBeUploadedList.joinToString(
                            prefix = "",
                            postfix = "",
                            separator = ","
                        )

                    snackbar = presentSnackBar(
                        String.format(
                            "Video size error",
                            "{\n$listOfFiles}"
                        )
                    )
                }
            }
        }
    }

    // Set imported image to grid view
    private fun setSelectedImageToAdapter(uri: Uri) {

        // add into item list
        val uploadedItem = UploadedItem(uri, false)
        if (::items.isInitialized.not()) items = HashMap()
        uploadedItem.itemPosition = items.size

        if (items.containsKey(uri.toString()).not()) {
            // items.put("z", NextItem())
            items[uri.toString()] = uploadedItem
            setList()
        }
    }

    private fun setList() {
        val list = items.values.toMutableList()
        mediaAdapter.submitList(list)
        mediaAdapter.notifyDataSetChanged()
    }


    /**
     *  Filter out media images which can't be uploaded because of size restriction
     */
    private fun isMediaHasValidSize(context: Context, uri: Uri): Boolean {

        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.let {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                it.moveToFirst()
                val type = context.contentResolver.getType(uri).orEmpty()
                val size = it.getLong(sizeIndex)
                val nameOfFile = it.getString(nameIndex).toString()
                Timber.i("Selected file type : $type, is the size exceeded : $size > ${AppConstants.UPLOAD_IMAGE_SIZE}")

                if (type.contains("image/jpeg")) {
                    if (size > AppConstants.UPLOAD_IMAGE_SIZE) {
                        filesCantBeUploadedList.add("Image : $nameOfFile \n")
                        return false
                    }
                }
            }
        } finally {
            cursor?.close()
        }
        return true
    }

    fun showMediaGallery() {
        if (UtilPermission.hasReadWritePermission(requireActivity())) {
            MediaPicker(requireActivity()).getImagePickerSelectionPanel()
        } else {
            UtilPermission.requestForReadWritePermission(requireActivity())
        }
    }
}
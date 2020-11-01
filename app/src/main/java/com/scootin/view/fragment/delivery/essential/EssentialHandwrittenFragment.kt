package com.scootin.view.fragment.delivery.essential

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.HandWrittenGroceryListBinding
import com.scootin.network.glide.GlideApp
import com.scootin.util.constants.AppConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.util.ui.FileUtils
import com.scootin.util.ui.MediaPicker
import com.scootin.util.ui.UtilPermission
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class EssentialHandwrittenFragment : Fragment(R.layout.hand_written_grocery_list) {
    private var binding by autoCleared<HandWrittenGroceryListBinding>()
    private val viewModel: CategoriesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HandWrittenGroceryListBinding.bind(view)
        binding.uploadPhoto.setOnClickListener {
            onClickOfUploadMedia()
        }

        viewModel.filePathLiveData.observe(viewLifecycleOwner, Observer {
            Timber.i("EssentialHandwrittenFragment response = ${it.code()} ${it.isSuccessful}")
        })
        binding.back.setOnClickListener { findNavController().popBackStack() }
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
            context?.let { context ->
                GlideApp.with(requireContext()).load(intent.data).into(binding.receiverPhotoBox)
                intent.data?.let {
                    viewModel.filePath(it)

                }
            }
        }
    }

}
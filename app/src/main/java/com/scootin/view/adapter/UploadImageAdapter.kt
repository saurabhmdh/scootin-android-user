package com.scootin.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide

import com.scootin.databinding.SelectedImageVideoBinding
import com.scootin.extensions.updateVisibility
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.util.interfaces.IMediaItem
import com.scootin.util.media.UploadedItem


class UploadImageAdapter(
    val appExecutors: AppExecutors,
    val imageAdapterClickListener: ImageAdapterClickLister
) : DataBoundListAdapter<IMediaItem, SelectedImageVideoBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<IMediaItem>() {
        override fun areItemsTheSame(
            oldItem: IMediaItem,
            newItem: IMediaItem
        ): Boolean {
            return oldItem.itemPosition == newItem.itemPosition
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: IMediaItem,
            newItem: IMediaItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): SelectedImageVideoBinding =
        SelectedImageVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: SelectedImageVideoBinding,
        item: IMediaItem,
        position: Int,
        isLast: Boolean
    ) {
        item as UploadedItem
        Glide.with(binding.userSelectedImage).load(item.uri).into(binding.userSelectedImage)
        if (!item.isNewlyAdded) {
            item.isNewlyAdded = true
            imageAdapterClickListener.onImportingImage(binding, item)
        }

        if (item.mediaStatus == Status.ERROR)
            binding.retryOverlay.updateVisibility(true)
        else
            binding.retryOverlay.updateVisibility(false)

        binding.retryAgain.setOnClickListener {
            binding.retryOverlay.updateVisibility(false)
            imageAdapterClickListener.onImportingImage(binding, item)
        }

        binding.discardImage.setOnClickListener {
            imageAdapterClickListener.onDiscardButtonClick(item)
        }
    }

    interface ImageAdapterClickLister {
        fun onImportingImage(
            binding: SelectedImageVideoBinding,
            item: UploadedItem
        )

        fun onDiscardButtonClick(item: UploadedItem)
        fun onUploadMoreButtonClick(view: View)
    }
}
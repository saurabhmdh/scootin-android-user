package com.scootin.util.media

import android.net.Uri
import androidx.annotation.Keep
import com.scootin.network.api.Status
import com.scootin.util.interfaces.IMediaItem
import kotlinx.coroutines.Job

@Keep
data class UploadedItem(
    var uri: Uri,
    var isNewlyAdded: Boolean
) : IMediaItem {

    override var itemPosition: Int = -1

    override var mediaId = ""

    override var mediaStatus = Status.LOADING

    override var mediaOnGoingJobId = Job()
}

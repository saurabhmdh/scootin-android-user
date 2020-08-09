package com.scootin.util.interfaces

import com.scootin.network.api.Status
import kotlinx.coroutines.CompletableJob

interface IMediaItem {

    var itemPosition: Int
    var mediaId: String
    var mediaStatus: Status
    var mediaOnGoingJobId: CompletableJob
}

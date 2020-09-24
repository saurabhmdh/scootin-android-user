package com.scootin.util.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.graphics.TypefaceCompatUtil.getTempFile
import com.scootin.util.constants.AppConstants
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList

class MediaPicker(val context: Activity) {

    fun getImagePickerSelectionPanel() {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fname = "random_$timeStamp"

        val sdImageMainDirectory = File(storageDir, fname)
        val outputFileUri = Uri.fromFile(sdImageMainDirectory)

        // Camera.
        val mediaIntents = ArrayList<Intent>()
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val packageManager = context.packageManager
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val packageName = res.activityInfo.packageName
            val intent = Intent(captureIntent)
            intent.component =
                ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(packageName)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            // cameraIntents.add(intent) // we are not adding camera, it is made for future requirement
        }

        // Gallery.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        // Filesystem.
        val fsIntent = Intent()
        fsIntent.type = "image/*,video/*"

        fsIntent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
        fsIntent.action = Intent.ACTION_GET_CONTENT
        fsIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        mediaIntents.add(fsIntent)

        // Create the Chooser
        val chooserIntent = Intent.createChooser(galleryIntent, "Select Source")
        chooserIntent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
        chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        chooserIntent.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            mediaIntents.toTypedArray<Parcelable>()
        )

        ActivityCompat.startActivityForResult(
            context as Activity,
            chooserIntent,
            AppConstants.RESULT_LOAD_IMAGE_VIDEO,
            null
        )
    }

    @SuppressLint("RestrictedApi")
    fun getImageUrl(
        context: Context,
        imageReturnedIntent: Intent?
    ): Uri? {
        val imageFile = getTempFile(context)
        val selectedImage: Uri?

        val isCamera = imageReturnedIntent == null ||
            imageReturnedIntent.data == null ||
            imageReturnedIntent.data!!.toString().contains(imageFile!!.toString())
        if (isCamera) {
            /** CAMERA  */
            selectedImage = Uri.fromFile(imageFile)
        } else {
            /** ALBUM  */
            selectedImage = imageReturnedIntent!!.data
        }
        Timber.i("selectedImage: " + selectedImage!!)

        return selectedImage
    }
}

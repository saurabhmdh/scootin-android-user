package com.scootin.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.scootin.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val REQUEST_UPDATE = 100
    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }
    private var isRunning = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        checkForUpdates()
    }

    private fun checkForUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            handleUpdate(appUpdateManager, it)
        }
    }

    private fun handleUpdate(manager: AppUpdateManager, info: AppUpdateInfo) {
        if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            && info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
            isRunning = true
            updateApplication(manager, info)
        }
    }

    private fun updateApplication(manager: AppUpdateManager, info: AppUpdateInfo) {
        manager.startUpdateFlowForResult(info, AppUpdateType.IMMEDIATE, this, REQUEST_UPDATE)
    }


    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                Timber.i("onResume -> update is in progress..")
                isRunning = true
                updateApplication(appUpdateManager, appUpdateInfo)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_UPDATE && resultCode == Activity.RESULT_CANCELED) {
            finish()
            isRunning = false
        }
    }

    fun isRunning() = isRunning
}
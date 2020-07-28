package com.scootin.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.scootin.R
import com.scootin.databinding.ActivitySplashBinding
import com.scootin.viewmodel.home.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        setUi()
    }

    private fun setUi() {
        val video = Uri.parse("android.resource://" + packageName.toString() + "/" + R.raw.video)
        binding.videoView.apply {
            setVideoURI(video)
            setOnCompletionListener { startNextActivity() }
            start()
        }
    }

    private fun startNextActivity() {
        if (isFinishing) return

        viewModel.firstLaunch().observe(this) {
            Timber.i("First launch.. $it")
            //Check for first time launch
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}
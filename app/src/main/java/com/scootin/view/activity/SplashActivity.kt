package com.scootin.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scootin.R
import com.scootin.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        setUi()
    }

    private fun setUi() {
        val video =
            Uri.parse(
                "android.resource://" + packageName
                    .toString() + "/" + R.raw.video
            )
        binding.videoView.apply {
            setVideoURI(video)
            setOnCompletionListener { startNextActivity() }
            start()
        }
    }

    private fun startNextActivity() {
        if (isFinishing) return
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
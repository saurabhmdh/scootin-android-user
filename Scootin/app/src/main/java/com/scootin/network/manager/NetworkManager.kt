package com.scootin.network.manager

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.scootin.BuildConfig
import com.scootin.util.constants.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkManager @Inject constructor(
    private val context: Context
) {

    fun getClient(): OkHttpClient {

        val builder = OkHttpClient.Builder().build().newBuilder()

        builder.takeIf {
             BuildConfig.DEBUG
        }?.also {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            it.addInterceptor(loggingInterceptor)
            it.addInterceptor(ChuckerInterceptor(context))
        }

        builder.apply {
            readTimeout(AppConstants.TIMEOUT_SECOND.toLong(), TimeUnit.SECONDS)
            connectTimeout(AppConstants.TIMEOUT_SECOND.toLong(), TimeUnit.SECONDS)
        }

        return builder.build()

    }

}
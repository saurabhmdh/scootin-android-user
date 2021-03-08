package com.scootin.network.manager

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.scootin.BuildConfig
import com.scootin.util.constants.AppConstants
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.net.ConnectException
import java.net.ProtocolException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkManager @Inject constructor(
    private val context: Context,
    private val tokenAuthenticator: TokenAuthenticator
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

        builder.interceptors().add(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request = chain.request()
                return try {
                    chain.proceed(request)
                } catch (e: Exception) {
                    when(e) {
                        is ProtocolException, is UnknownHostException, is TimeoutException, is ConnectException -> {
                            Timber.i("There is network exception ${e.message}")
                        }
                    }
                    Response.Builder().code(404).body("".toResponseBody("application/json".toMediaTypeOrNull()))
                        .message("")
                        .protocol(Protocol.HTTP_1_1)
                        .request(Request.Builder().url("http://localhost/").build()).build()
                }
            }
        })

        builder.authenticator(tokenAuthenticator)
        return builder.build()

    }

}
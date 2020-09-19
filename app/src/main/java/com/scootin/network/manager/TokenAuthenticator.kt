package com.scootin.network.manager

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.scootin.BuildConfig
import com.scootin.database.dao.CacheDao
import com.scootin.database.table.Cache
import com.scootin.network.AppExecutors

import com.scootin.network.api.ApiAuthenticationServices
import com.scootin.network.response.login.ResponseUser
import com.scootin.network.retrofit.RetrofitBuilderFactory
import com.scootin.util.constants.AppConstants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenAuthenticator @Inject constructor(
    private val context: Context,
    private val appExecutors: AppExecutors,
    private val cacheDao: CacheDao,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.i("TokenAuthenticator -> response ${response}")
        return generateRequest(response)
    }

    //May be 3 time of retry
    @Synchronized
    private fun generateRequest(response: Response): Request? {
        val user: ResponseUser? = createAuthorizationService().refreshToken(
            mapOf("auth" to AppHeaders.token)
        ).execute().body()

        user?.let {
            appExecutors.diskIO().execute {
                val updatedUserInfo = Gson().toJson(user)
                cacheDao.insert(Cache(AppConstants.USER_INFO, updatedUserInfo))
            }
            AppHeaders.updateUserData(user)
            return createRequestWithNewTokens(user.token, response)
        }
        return response.request
    }




    private fun createAuthorizationService(): ApiAuthenticationServices {
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

        return RetrofitBuilderFactory.createRetrofit(builder.build()).create(ApiAuthenticationServices::class.java)
    }

    private fun createRequestWithNewTokens(token: String, response: Response): Request {
        return response.request.newBuilder()
            .header(AppConstants.AUTHORIZATION, AppHeaders.PREFIX + token)
            .method(response.request.method, response.request.body)
            .build()
    }
}
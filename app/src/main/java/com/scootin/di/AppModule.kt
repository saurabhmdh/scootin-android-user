package com.scootin.di

import android.app.Application
import android.content.Context
import com.scootin.network.api.APIService
import com.scootin.network.manager.NetworkManager
import com.scootin.network.retrofit.RetrofitBuilderFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        RetrofitBuilderFactory.createRetrofit(okHttpClient)

    @Provides
    @Singleton
    internal fun provideOkHttpClient(
        networkManager: NetworkManager
    ): OkHttpClient = networkManager.getClient()


    @Singleton
    @Provides
    fun provideAPIService(retrofit: Retrofit): APIService = retrofit.create(APIService::class.java)
}
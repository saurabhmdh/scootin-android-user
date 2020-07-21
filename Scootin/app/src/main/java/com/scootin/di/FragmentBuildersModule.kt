package com.scootin.di

import com.scootin.view.fragment.home.HomeFragment
import com.scootin.view.fragment.home.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeCategoryFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeSplashFragment(): SplashFragment
}
package com.scootin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scootin.viewmodel.ViewModelFactory
import com.scootin.viewmodel.home.HomeFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeFragmentViewModel::class)
    protected abstract fun movieListViewModel(moviesListViewModel: HomeFragmentViewModel): ViewModel
}
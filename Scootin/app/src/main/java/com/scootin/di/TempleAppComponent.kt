package com.scootin.di

import android.app.Application
import com.scootin.TempleApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TempleAppModule::class,
        AndroidInjectionModule::class,
        TempleActivityModule::class,
        WorkerModule::class]
)
interface TempleAppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): TempleAppComponent
    }

    fun inject(app: TempleApplication)
}
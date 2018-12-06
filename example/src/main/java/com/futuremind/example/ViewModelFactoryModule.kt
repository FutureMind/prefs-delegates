package com.futuremind.example

import android.arch.lifecycle.ViewModelProvider
import com.futuremind.daggerutils.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

}

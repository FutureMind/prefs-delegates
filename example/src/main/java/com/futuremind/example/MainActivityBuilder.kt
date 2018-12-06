package com.futuremind.example

import android.arch.lifecycle.ViewModel
import com.futuremind.daggerutils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class MainActivityBuilder {

    @ContributesAndroidInjector
    internal abstract fun contributeActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}

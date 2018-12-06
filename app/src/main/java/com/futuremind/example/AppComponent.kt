package com.futuremind.example

import com.futuremind.preferencesdelegates.SharedPrefsModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            ViewModelFactoryModule::class,
            SharedPrefsModule::class,
            ContextModule::class,
            MainActivityBuilder::class
        ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}

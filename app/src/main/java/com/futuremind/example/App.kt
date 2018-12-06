package com.futuremind.example

import android.app.Activity
import android.app.Application
import com.futuremind.daggerutils.AndroidComponentsInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App: Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    lateinit var appComponent: AppComponent

    override fun activityInjector() = activityInjector

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).build()

        AndroidComponentsInjector.init(this)
    }
}
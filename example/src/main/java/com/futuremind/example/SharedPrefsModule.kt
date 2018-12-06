package com.futuremind.example

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPrefsModule {

    @Singleton
    @Provides
    fun providePreferences(cxt: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(cxt)
}
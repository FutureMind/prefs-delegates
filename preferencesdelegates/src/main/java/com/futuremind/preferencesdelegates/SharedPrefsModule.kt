package com.futuremind.preferencesdelegates

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import javax.inject.Singleton
import dagger.Provides

@Module
class SharedPrefsModule {

    @Singleton
    @Provides
    fun providePreferences(cxt: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(cxt)
}
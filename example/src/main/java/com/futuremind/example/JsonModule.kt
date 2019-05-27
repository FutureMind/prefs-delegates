package com.futuremind.example

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class JsonModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()
}
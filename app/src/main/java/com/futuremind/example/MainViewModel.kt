package com.futuremind.example

import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(val testStore: TestStore) : ViewModel() {
    fun saveToken(token: String) {
        testStore.saveToken.prefsDelegate = token
    }

}
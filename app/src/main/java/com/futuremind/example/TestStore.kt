package com.futuremind.example

import com.futuremind.preferencesdelegates.PrefsObserver
import javax.inject.Inject

class TestStore @Inject constructor(val saveToken: PrefsObserver<String>) {

    companion object {
        private const val TOKEN_KEY = "token_key"
    }

    init {
        saveToken.PREFS_KEY = TOKEN_KEY
    }

}
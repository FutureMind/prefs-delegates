package com.futuremind.example

import android.content.SharedPreferences
import com.futuremind.preferencesdelegates.PrefsObserver
import com.futuremind.preferencesdelegates.long
import com.futuremind.preferencesdelegates.valueDelegate
import javax.inject.Inject

class TestStore @Inject constructor(prefs: SharedPreferences) {

    companion object {
        private const val AGE_KEY = "age_key"
        private const val TOKEN_KEY = "token_key"
    }

    var age by prefs.long(AGE_KEY)

    var token by prefs.valueDelegate<String>(TOKEN_KEY, {}, "")



}
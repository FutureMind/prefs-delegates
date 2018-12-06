package com.futuremind.example

import android.content.SharedPreferences
import com.futuremind.preferencesdelegates.PreferencesObserver
import com.futuremind.preferencesdelegates.prefsDelegate
import javax.inject.Inject

class TestStore @Inject constructor(prefs: SharedPreferences) {

    companion object {
        private const val AGE_KEY = "age_key"
        private const val TOKEN_KEY = "token_key"
    }

    var age by prefs.prefsDelegate<Int>(AGE_KEY, -1)

    var token = PreferencesObserver(TOKEN_KEY, prefs, String::class.java, "")

}
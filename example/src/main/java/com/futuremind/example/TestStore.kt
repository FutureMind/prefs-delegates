package com.futuremind.example

import android.content.SharedPreferences
import com.futuremind.preferencesdelegates.enum
import com.futuremind.preferencesdelegates.int
import com.futuremind.preferencesdelegates.json
import com.futuremind.preferencesdelegates.rx.observableString
import com.squareup.moshi.Moshi
import javax.inject.Inject

class TestStore @Inject constructor(prefs: SharedPreferences, moshi: Moshi) {

    companion object {
        private const val AGE_KEY = "age_key"
        private const val TOKEN_KEY = "token_key"
        private const val ENUM_KEY = "enum_key"
        private const val PERSON_KEY = "preson_key"
    }

    enum class SomeEnum { AWESOME, NICE, CRAP }

    var age: Int by prefs.int(AGE_KEY, -1)

    var enum: SomeEnum by prefs.enum(ENUM_KEY, SomeEnum.AWESOME)

    var person: Person? by prefs.json(PERSON_KEY, moshi)

    var token = prefs.observableString(TOKEN_KEY, "")

}

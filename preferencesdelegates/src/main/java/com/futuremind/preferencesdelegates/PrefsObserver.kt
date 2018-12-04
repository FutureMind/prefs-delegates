package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.processors.BehaviorProcessor

class PrefsObserver<T> constructor(prefs: SharedPreferences, clazz: Class<T>) {

    private val processor = BehaviorProcessor.create<T>()

    var PREFS_KEY: String = "prefs_key"

    var prefsDelegate by when (clazz) {
        String::class.java -> prefs.string<T>(PREFS_KEY, { fieldValue -> processor.onNext(fieldValue as T) })
        Int::class.java -> prefs.integer<T>(PREFS_KEY, { fieldValue -> processor.onNext(fieldValue as T) })
        Float::class.java -> prefs.float<T>(PREFS_KEY, { fieldValue -> processor.onNext(fieldValue as T) })
        Long::class.java -> prefs.long<T>(PREFS_KEY, { fieldValue -> processor.onNext(fieldValue as T) })
        else -> prefs.boolean<T>(PREFS_KEY, { fieldValue -> processor.onNext(fieldValue as T) })
    }

    fun observe(): Observable<T> = processor.toObservable().share()
}



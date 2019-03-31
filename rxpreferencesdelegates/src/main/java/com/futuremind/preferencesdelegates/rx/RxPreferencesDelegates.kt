package com.futuremind.preferencesdelegates.rx

import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import kotlin.reflect.KProperty

inline fun <reified T> SharedPreferences.rxPrefsDelegate(
        prefsKey: String,
        defaultValue: T
): ObservableProperty<Any, T> {
    val prefs = this

    return object: ObservableProperty<Any, T> {
        val subject: Subject<T> = BehaviorSubject.create()

        init {
            subject.onNext(when(defaultValue) {
                is Boolean -> prefs.getBoolean(prefsKey, defaultValue) as T
                is Int -> prefs.getInt(prefsKey, defaultValue) as T
                is Long -> prefs.getLong(prefsKey, defaultValue) as T
                is Float -> prefs.getFloat(prefsKey, defaultValue) as T
                is String -> prefs.getString(prefsKey, defaultValue) as T
                else -> throw IllegalAccessException("Shared preferences don't support type ${T::class.java.simpleName}")
            })
        }

        override fun getValue(thisRef: Any, property: KProperty<*>): Observable<T> {
            return subject
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Observable<T>) {
            val rawValue = value.blockingFirst()
            when(rawValue) {
                is Boolean -> prefs.edit().putBoolean(prefsKey, rawValue).apply()
                is Int -> prefs.edit().putInt(prefsKey, rawValue).apply()
                is Long -> prefs.edit().putLong(prefsKey, rawValue).apply()
                is Float -> prefs.edit().putFloat(prefsKey, rawValue).apply()
                is String -> prefs.edit().putString(prefsKey, rawValue).apply()
                else -> throw IllegalAccessException()
            }
            subject.onNext(rawValue)
        }
    }
}

interface ObservableProperty<in R, T> {

    operator fun getValue(thisRef: R, property: KProperty<*>): Observable<T>

    operator fun setValue(thisRef: R, property: KProperty<*>, value: Observable<T>)
}

fun <T> T.justIt(): Observable<T> = Observable.just(this)
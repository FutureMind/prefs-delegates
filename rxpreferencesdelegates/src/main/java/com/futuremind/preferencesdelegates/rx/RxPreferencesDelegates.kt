@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package com.futuremind.preferencesdelegates.rx

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun SharedPreferences.observableBoolena(prefsKey: String, defaultValue: Boolean = false) =
        rxPrefsDelegate(prefsKey, defaultValue, this::getBoolean, Editor::putBoolean)

fun SharedPreferences.observableLong(prefsKey: String, defaultValue: Long = 0L) =
        rxPrefsDelegate(prefsKey, defaultValue, this::getLong, Editor::putLong)

fun SharedPreferences.observableInt(prefsKey: String, defaultValue: Int = 0) =
        rxPrefsDelegate(prefsKey, defaultValue, this::getInt, Editor::putInt)

fun SharedPreferences.observableFloat(prefsKey: String, defaultValue: Float = 0f) =
        rxPrefsDelegate(prefsKey, defaultValue, this::getFloat, Editor::putFloat)

fun SharedPreferences.observableString(prefsKey: String, defaultValue: String? = null) =
        rxPrefsDelegate(prefsKey, defaultValue, this::getString, Editor::putString)

fun SharedPreferences.observableStringSet(prefsKey: String, defaultValue: Set<String>? = null) =
        rxPrefsDelegate(prefsKey, defaultValue, this::getStringSet, Editor::putStringSet)

fun <T> SharedPreferences.rxPrefsDelegate(
        prefsKey: String,
        defaultValue: T,
        readFunc: (String, T) -> T,
        writeFunc: Editor.(String, T) -> Editor
): ReadWriteProperty<Any, Observable<T>> {
    val prefs = this

    return object: ReadWriteProperty<Any, Observable<T>> {
        private val subject = BehaviorSubject.create<T>()

        init { subject.onNext(readFunc(prefsKey, defaultValue)) }

        override fun getValue(thisRef: Any, property: KProperty<*>): Observable<T> =
            subject

        override fun setValue(thisRef: Any, property: KProperty<*>, valueObservable: Observable<T>) {
            valueObservable
                .doOnNext { value ->
                    prefs.edit().writeFunc(prefsKey, value)
                    subject.onNext(value)
                }
                .subscribe()
        }
    }
}

fun <T> T.justIt(): Observable<T> = Observable.just(this)
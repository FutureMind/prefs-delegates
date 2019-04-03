@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package com.futuremind.preferencesdelegates.rx

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.lang.ClassCastException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

fun SharedPreferences.observableBoolean(prefsKey: String, defaultValue: Boolean = false) =
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
): RxProperty<Any, T> {
    val prefs = this

    return object: RxProperty<Any, T> {
        private val subject = BehaviorSubject.create<T>()
        private var lastValue: T

        init {
            lastValue = readFunc(prefsKey, defaultValue)
            subject.onNext(lastValue)
        }

        override fun getValue(thisRef: Any, property: KProperty<*>) =
            readFunc(prefsKey, defaultValue)
                .also {
                    if (it != lastValue) {
                        subject.onNext(it)
                        lastValue = it
                    }
                }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            prefs.edit().writeFunc(prefsKey, value).apply()
            subject.onNext(value)
        }

        override fun asObservable(): Observable<T> = subject
    }
}

interface RxProperty<in R, T>: ReadWriteProperty<R, T> {
    fun asObservable(): Observable<T>
}

@Suppress("UNCHECKED_CAST")
fun <T> KProperty0<T>.asObservable(): Observable<T> {

    fun notSupportingRx(): Nothing =
            throw IllegalAccessException("property ${this.name} does not support Rx")

    try {
        val delegate = this
                .apply { isAccessible = true }
                .getDelegate() as? RxProperty<Any, T>

        return delegate?.asObservable() ?: notSupportingRx()
    }
    catch (e: ClassCastException) {
        notSupportingRx()
    }
}
package com.futuremind.preferencesdelegates.rx

import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

/**
 * Produces [ObservablePreference] for type [Boolean]
 * @param prefsKey the key preference to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided if key is not present in [SharedPreferences]
 */
fun SharedPreferences.observableBoolean(prefsKey: String, defaultValue: Boolean = false) =
        ObservablePreference(this, prefsKey, defaultValue, SharedPreferences::getBoolean, SharedPreferences.Editor::putBoolean)

/**
 * Produces [ObservablePreference] for type [Int]
 * @param prefsKey the key preference to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided if key is not present in [SharedPreferences]
 */
fun SharedPreferences.observableLong(prefsKey: String, defaultValue: Long = 0L) =
        ObservablePreference(this, prefsKey, defaultValue, SharedPreferences::getLong, SharedPreferences.Editor::putLong)

/**
 * Produces [ObservablePreference] for type [Long]
 * @param prefsKey the key preference to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided if key is not present in [SharedPreferences]
 */
fun SharedPreferences.observableInt(prefsKey: String, defaultValue: Int = 0) =
        ObservablePreference(this, prefsKey, defaultValue, SharedPreferences::getInt, SharedPreferences.Editor::putInt)

/**
 * Produces [ObservablePreference] for type [Float]
 * @param prefsKey the key preference to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided if key is not present in [SharedPreferences]
 */
fun SharedPreferences.observableFloat(prefsKey: String, defaultValue: Float = 0f) =
        ObservablePreference(this, prefsKey, defaultValue, SharedPreferences::getFloat, SharedPreferences.Editor::putFloat)

/**
 * Produces [ObservablePreference] for type [String]
 * @param prefsKey the key preference to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided if key is not present in [SharedPreferences]
 */
fun SharedPreferences.observableString(prefsKey: String, defaultValue: String = "") =
        ObservablePreference(this, prefsKey, defaultValue, SharedPreferences::getString, SharedPreferences.Editor::putString)

/**
 * Produces [ObservablePreference] for type [Set]<[String]>
 * @param prefsKey the key preference to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided if key is not present in [SharedPreferences]
 */
fun SharedPreferences.observableStringSet(prefsKey: String, defaultValue: Set<String> = emptySet()) =
        ObservablePreference(this, prefsKey, defaultValue, SharedPreferences::getStringSet, SharedPreferences.Editor::putStringSet)


/**
 * Class representing observable shared preference.
 * NOTE: When using this class, do not change the underlying preference from outside world,
 * because the changes you made won't be noticed by the observers.
 * @param prefs the [SharedPreferences] to associate the preference with
 * @param prefsKey the key preference to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided if key is not present in [SharedPreferences]
 * @param readFunc function which will obtain preference value from [SharedPreferences] object given as receiver
 * @param writeFunc function which will save preference value into [SharedPreferences.Editor] object given as receiver
 */
class ObservablePreference<T: Any>(
    private val prefs: SharedPreferences,
    private val prefsKey: String,
    private val defaultValue: T,
    private val readFunc: SharedPreferences.(String, T) -> T,
    private val writeFunc: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
) {
    private var emitter: ObservableEmitter<T>? = null
    private val observable = Observable.create<T> {
        emitter = it
        emitter?.onNext(prefs.readFunc(prefsKey, defaultValue))
    }.distinctUntilChanged().replay(1).autoConnect()

    /**
     * Get the [Observable] which will notify subscribers about each modification of the preference.
     */
    fun observable(): Observable<T> = observable

    /**
     * Get value of the shared preference, it is guaranteed that this method return an up-to-date value.
     * If the value has externally changed since the last read access then subscribers will be notified.
     */
    fun get() = prefs.readFunc(prefsKey, defaultValue).also { emitter?.onNext(it) }

    /**
     * Save the value in associated [SharedPreferences] object.
     * If the value has changed, then subscribers will be notified.
     * @param value the value to be saved
     */
    fun save(value: T) {
        prefs.edit().writeFunc(prefsKey, value).apply()
        emitter?.onNext(value)
    }
}
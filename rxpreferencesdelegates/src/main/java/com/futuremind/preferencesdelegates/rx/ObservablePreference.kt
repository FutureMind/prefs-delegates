package com.futuremind.preferencesdelegates.rx

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
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
 * Produces property delegate for custom enum type, which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 */
inline fun <reified E : Enum<E>> SharedPreferences.observableEnum(prefsKey: String, defaultValue: E) =
        ObservablePreference(
                this,
                prefsKey,
                defaultValue,
                { key, default -> java.lang.Enum.valueOf(E::class.java, this.getString(key, default.name)) },
                { key, value -> this.putString(key, value!!.name) }
        )

/**
 * Produces property delegate for any type, which will be stored in given [SharedPreferences] object in JSON format
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 * @param moshi the [Moshi] library object for JSON parsing
 */
inline fun <reified T> SharedPreferences.observableJson(prefsKey: String, defaultValue: T?, moshi: Moshi): ObservablePreference<T?> {
    val jsonAdapter = moshi.adapter(T::class.java)
    val defaultJson = if (defaultValue != null) jsonAdapter.toJson(defaultValue) else ""
    return ObservablePreference(
            this,
            prefsKey,
            defaultValue,
            { key, default: T? ->
                try {
                    val json = this.getString(key, defaultJson)
                    jsonAdapter.fromJson(json!!)!!
                } catch (e: Exception) {
                    default
                }
            },
            { key, value: T? -> this.putString(key, jsonAdapter.toJson(value)) }
    )
}


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
class ObservablePreference<T>(
    private val prefs: SharedPreferences,
    private val prefsKey: String,
    private val defaultValue: T,
    private val readFunc: SharedPreferences.(key: String, default: T) -> T,
    private val writeFunc: SharedPreferences.Editor.(key: String, default: T) -> SharedPreferences.Editor
) {
    private var emitter: ObservableEmitter<T>? = null
    private val observable = Observable.create<T> {
        emitter = it
        if (defaultValue != null) emitter?.onNext(prefs.readFunc(prefsKey, defaultValue))
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

package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.squareup.moshi.Moshi
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Produces property delegate for [Boolean], which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 */
fun SharedPreferences.boolean(prefsKey: String, defaultValue: Boolean = false) =
        prefsDelegate(prefsKey, defaultValue, SharedPreferences::getBoolean, SharedPreferences.Editor::putBoolean)

/**
 * Produces property delegate for [Long], which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 */
fun SharedPreferences.long(prefsKey: String, defaultValue: Long = 0L) =
        prefsDelegate(prefsKey, defaultValue, SharedPreferences::getLong, SharedPreferences.Editor::putLong)

/**
 * Produces property delegate for [Int], which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 */
fun SharedPreferences.int(prefsKey: String, defaultValue: Int = 0) =
        prefsDelegate(prefsKey, defaultValue, SharedPreferences::getInt, SharedPreferences.Editor::putInt)

/**
 * Produces property delegate for [Float], which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 */
fun SharedPreferences.float(prefsKey: String, defaultValue: Float = 0f) =
        prefsDelegate(prefsKey, defaultValue, SharedPreferences::getFloat, SharedPreferences.Editor::putFloat)

/**
 * Produces property delegate for [String], which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 */
fun SharedPreferences.string(prefsKey: String, defaultValue: String? = null) =
        prefsDelegate(prefsKey, defaultValue, SharedPreferences::getString, SharedPreferences.Editor::putString)

/**
 * Produces property delegate for [Set]<[String]>, which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 */
fun SharedPreferences.stringSet(prefsKey: String, defaultValue: Set<String>? = null) =
        prefsDelegate(prefsKey, defaultValue, SharedPreferences::getStringSet, SharedPreferences.Editor::putStringSet)

/**
 * Produces property delegate for custom enum type, which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 */
inline fun <reified E : Enum<E>> SharedPreferences.enum(prefsKey: String, defaultValue: E) =
        prefsDelegate(
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
inline fun <reified T : Any?> SharedPreferences.json(prefsKey: String, defaultValue: T?, moshi: Moshi): ReadWriteProperty<Any, T?> {
    val jsonAdapter = moshi.adapter(T::class.java)
    val defaultJson = if (defaultValue != null) jsonAdapter.toJson(defaultValue) else ""
    return prefsDelegate(
            prefsKey,
            defaultValue,
            { key, default: T? ->
                try {
                    val json = this.getString(key, defaultJson)
                    jsonAdapter.fromJson(json!!)!!
                } catch (e: Exception) {default }
            },
            { key, value: T? -> this.putString(key, jsonAdapter.toJson(value)) }
    )
}

@PublishedApi internal fun <T> SharedPreferences.prefsDelegate(
        prefsKey: String,
        defaultValue: T,
        readFunc: SharedPreferences.(String, T) -> T,
        writeFunc: SharedPreferences.Editor.(String, T) -> Editor
): ReadWriteProperty<Any, T> {
    val prefs = this
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>) = prefs.readFunc(prefsKey, defaultValue)
        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = prefs.edit().writeFunc(prefsKey, value).apply()
    }
}
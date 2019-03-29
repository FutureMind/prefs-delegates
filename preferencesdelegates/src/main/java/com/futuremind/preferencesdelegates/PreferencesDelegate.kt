package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.squareup.moshi.Moshi
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Produces property delegate for [Boolean], which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if nothing is stored in [SharedPreferences]
 */
fun SharedPreferences.boolean(prefsKey: String, defaultValue: Boolean = false) =
        prefsDelegate(prefsKey, defaultValue, this::getBoolean, Editor::putBoolean)

/**
 * Produces property delegate for [Long], which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if nothing is stored in [SharedPreferences]
 */
fun SharedPreferences.long(prefsKey: String, defaultValue: Long = 0L) =
        prefsDelegate(prefsKey, defaultValue, this::getLong, Editor::putLong)

/**
 * Produces property delegate for [Int], which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if nothing is stored in [SharedPreferences]
 */
fun SharedPreferences.int(prefsKey: String, defaultValue: Int = 0) =
        prefsDelegate(prefsKey, defaultValue, this::getInt, Editor::putInt)

/**
 * Produces property delegate for [Float], which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if nothing is stored in [SharedPreferences]
 */
fun SharedPreferences.float(prefsKey: String, defaultValue: Float = 0f) =
        prefsDelegate(prefsKey, defaultValue, this::getFloat, Editor::putFloat)

/**
 * Produces property delegate for [String], which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if nothing is stored in [SharedPreferences]
 */
fun SharedPreferences.string(prefsKey: String, defaultValue: String? = null) =
        prefsDelegate(prefsKey, defaultValue, this::getString, Editor::putString)

/**
 * Produces property delegate for [Set]<[String]>, which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if nothing is stored in [SharedPreferences]
 */
fun SharedPreferences.stringSet(prefsKey: String, defaultValue: Set<String>? = null) =
        prefsDelegate(prefsKey, defaultValue, this::getStringSet, Editor::putStringSet)

/**
 * Produces property delegate for custom enum type, which will be stored in given [SharedPreferences] object
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if nothing is stored in [SharedPreferences]
 */
inline fun <reified TEnum : Enum<TEnum>> SharedPreferences.enum(prefsKey: String, defaultValue: TEnum) =
        prefsDelegate(
                prefsKey,
                defaultValue,
                { key, default -> java.lang.Enum.valueOf(TEnum::class.java, this.getString(key, default.name)) },
                { key, value -> this.putString(key, value!!.name) }
        )

/**
 * Produces property delegate for any type, which will be stored in given [SharedPreferences] object in JSON format
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if nothing is stored in [SharedPreferences]
 * @param moshi the [Moshi] library object for JSON parsing
 */
inline fun <reified TDataClass : Any?> SharedPreferences.json(prefsKey: String, defaultValue: TDataClass?, moshi: Moshi): ReadWriteProperty<Any, TDataClass?> {
    val jsonAdapter = moshi.adapter(TDataClass::class.java)
    val defaultJson = if (defaultValue != null) jsonAdapter.toJson(defaultValue) else ""
    return prefsDelegate(
            prefsKey,
            defaultValue,
            { key, default: TDataClass? ->
                try {
                    val json = this.getString(key, defaultJson)
                    jsonAdapter.fromJson(json!!)!!
                } catch (e: Exception) {default }
            },
            { key, value: TDataClass? -> this.putString(key, jsonAdapter.toJson(value)) }
    )
}

@PublishedApi internal fun <T> SharedPreferences.prefsDelegate(
        prefsKey: String,
        defaultValue: T,
        readFunc: (String, T) -> T,
        writeFunc: Editor.(String, T) -> Editor
): ReadWriteProperty<Any, T> = object : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = readFunc(prefsKey, defaultValue)
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = this@prefsDelegate.edit().writeFunc(prefsKey, value).apply()
}
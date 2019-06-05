package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Produces property delegate for nullable [Boolean?], which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @return value of the preference or `null` if given key is not present
 */
fun SharedPreferences.boolean(prefsKey: String) = boolean<Boolean?>(prefsKey, null)

/**
 * Produces property delegate for non-/nullable [Boolean], which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * When [defaultValue] is specified as `null`, then delegate always returns nullable [Boolean?] implicit.
 * Otherwise delegate returns non nullable [Boolean] implicit but field still can be declared as nullable [Boolean?].
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 * @return value of the preference or [defaultValue] if given key is not present
 */
inline fun <reified T : Boolean?> SharedPreferences.boolean(prefsKey: String, defaultValue: T): ReadWriteProperty<Any, T> =
        prefsDelegate<Boolean, T>(
                prefsKey,
                defaultValue,
                { getBoolean(it, false) },
                SharedPreferences.Editor::putBoolean
        )


/**
 * Produces property delegate for nullable [Int?], which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @return value of the preference or `null` if given key is not present
 */
fun SharedPreferences.int(prefsKey: String) = int<Int?>(prefsKey, null)

/**
 * Produces property delegate for non-/nullable [Int], which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * When [defaultValue] is specified as `null`, then delegate always returns nullable [Int?] implicit.
 * Otherwise delegate returns non nullable [Int] implicit but field still can be declared as nullable [Int?].
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 * @return value of the preference or [defaultValue] if given key is not present
 */
inline fun <reified T : Int?> SharedPreferences.int(prefsKey: String, defaultValue: T) =
        prefsDelegate<Int, T>(
                prefsKey,
                defaultValue,
                { getInt(it, 0) },
                SharedPreferences.Editor::putInt
        )


/**
 * Produces property delegate for nullable [Long?], which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @return value of the preference or `null` if given key is not present
 */
fun SharedPreferences.long(prefsKey: String) = long<Long?>(prefsKey, null)

/**
 * Produces property delegate for non-/nullable [Long], which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * When [defaultValue] is specified as `null`, then delegate always returns nullable [Long?] implicit.
 * Otherwise delegate returns non nullable [Long] implicit but field still can be declared as nullable [Long?].
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 * @return value of the preference or [defaultValue] if given key is not present
 */
inline fun <reified T : Long?> SharedPreferences.long(prefsKey: String, defaultValue: T) =
        prefsDelegate<Long, T>(
                prefsKey,
                defaultValue,
                { getLong(it, 0) },
                SharedPreferences.Editor::putLong
        )


/**
 * Produces property delegate for nullable [Float?], which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @return value of the preference or `null` if given key is not present
 */
fun SharedPreferences.float(prefsKey: String) = float<Float?>(prefsKey, null)

/**
 * Produces property delegate for non-/nullable [Float], which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * When [defaultValue] is specified as `null`, then delegate always returns nullable [Float?] implicit.
 * Otherwise delegate returns non nullable [Float] implicit but field still can be declared as nullable [Float?].
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 * @return value of the preference or [defaultValue] if given key is not present
 */
inline fun <reified T : Float?> SharedPreferences.float(prefsKey: String, defaultValue: T) =
        prefsDelegate<Float, T>(
                prefsKey,
                defaultValue,
                { getFloat(it, 0f) },
                SharedPreferences.Editor::putFloat
        )


/**
 * Produces property delegate for nullable [String?], which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @return value of the preference or `null` if given key is not present
 */
fun SharedPreferences.string(prefsKey: String) = string<String?>(prefsKey, null)

/**
 * Produces property delegate for non-/nullable [String], which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * When [defaultValue] is specified as `null`, then delegate always returns nullable [String?] implicit.
 * Otherwise delegate returns non nullable [String] implicit but field still can be declared as nullable [String?].
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 * @return value of the preference or [defaultValue] if given key is not present
 */
inline fun <reified T : String?> SharedPreferences.string(prefsKey: String, defaultValue: T) =
        prefsDelegate<String, T>(
                prefsKey,
                defaultValue,
                { getString(it, null) },
                SharedPreferences.Editor::putString
        )


/**
 * Produces property delegate for nullable [Set]<[String]>`?`, which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @return value of the preference or `null` if given key is not present
 */
fun SharedPreferences.stringSet(prefsKey: String) = stringSet<Set<String>?>(prefsKey, null)

/**
 * Produces property delegate for non-/nullable [Set]<[String]>, which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * When [defaultValue] is specified as `null`, then delegate always returns nullable [Set]<[String]>`?` implicit.
 * Otherwise delegate returns non nullable [Set]<[String]> implicit but field still can be declared as nullable [Set]<[String]>`?`.
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 * @return value of the preference or [defaultValue] if given key is not present
 */
inline fun <reified T : Set<String>?> SharedPreferences.stringSet(prefsKey: String, defaultValue: T) =
        prefsDelegate<Set<String>, T>(
                prefsKey,
                defaultValue,
                { getStringSet(it, null) },
                SharedPreferences.Editor::putStringSet
        )

/**
 * Produces property delegate for custom nullable [Enum] type, which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @return enum value of the preference or `null` if given key is not present
 */
inline fun <reified T : Enum<*>> SharedPreferences.enum(prefsKey: String) = enum<T?>(prefsKey, null)

/**
 * Produces property delegate for non-/nullable custom [Enum] type, which will be stored in given [SharedPreferences] object.
 * When `null` is assigned, the property is removed.
 *
 * When [defaultValue] is specified as `null`, then field type must be declared explicit as nullable custom [Enum?] type.
 * Otherwise delegate returns non nullable custom [Enum] type implicit but field still can be declared as nullable custom [Enum?] type.
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 * @return enum value of the preference or [defaultValue] if given key is not present
 */
inline fun <reified T : Enum<*>?> SharedPreferences.enum(prefsKey: String, defaultValue: T) =
        prefsDelegate<Enum<*>, T>(
                prefsKey,
                defaultValue,
                { key ->
                    val enumName = getString(key, null)
                    T::class.java.enumConstants.firstOrNull { it!!.name == enumName }
                },
                { key, value -> putString(key, value.name) }
        )


/**
 * Produces property delegate for any nullable type, which will be stored in given [SharedPreferences] object in JSON format.
 * When `null` is assigned, the property is removed.
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param moshi the [Moshi] library object for JSON parsing
 * @return value of the preference or null if given key is not present
 */
inline fun <reified T : Any?> SharedPreferences.json(prefsKey: String, moshi: Moshi) = json<T?>(prefsKey, moshi, null)

/**
 * Produces property delegate for any non-/nullable type, which will be stored in given [SharedPreferences] object in JSON format.
 * When `null` is assigned, the property is removed.
 *
 * When [defaultValue] is specified as `null`, then field type must be declared explicit as nullable type.
 * Otherwise delegate returns non nullable type implicit but field still can be declared as nullable type.
 *
 * @param prefsKey the key property to be referred with in [SharedPreferences]
 * @param moshi the [Moshi] library object for JSON parsing
 * @param defaultValue the value to be provided to property if key is not present in [SharedPreferences]
 * @return value of the preference or [defaultValue] if given key is not present
 */
inline fun <reified T : Any?> SharedPreferences.json(prefsKey: String, moshi: Moshi, defaultValue: T): ReadWriteProperty<Any, T> {
    val jsonAdapter = moshi.adapter(T::class.java)
    return prefsDelegate<Any, T>(
            prefsKey,
            defaultValue,
            { key ->
                try {
                    jsonAdapter.fromJson(getString(key, null)!!)
                } catch (e: Exception) {
                    null
                }
            },
            { key, value -> putString(key, jsonAdapter.toJson(value as T)) }
    )
}


@PublishedApi
internal fun <NonNullableT : Any, T : NonNullableT?> SharedPreferences.prefsDelegate(
        key: String,
        defaultValue: T,
        readFunc: SharedPreferences.(key: String) -> NonNullableT?,
        writeFunc: SharedPreferences.Editor.(key: String, value: NonNullableT) -> SharedPreferences.Editor
): ReadWriteProperty<Any, T> {
    val prefs = this
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
                if (prefs.contains(key)) prefs.readFunc(key) as? T ?: defaultValue
                else defaultValue

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
                prefs.edit().apply {
                    if (value != null) writeFunc(key, value)
                    else remove(key)
                }.apply()
    }
}

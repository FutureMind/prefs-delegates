package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KFunction2
import kotlin.reflect.KProperty

fun SharedPreferences.boolean(prefsKey: String, defaultValue: Boolean = false) =
        prefsDelegate(prefsKey, defaultValue, this::getBoolean, Editor::putBoolean)

fun SharedPreferences.long(prefsKey: String, defaultValue: Long = 0L) =
        prefsDelegate(prefsKey, defaultValue, this::getLong, Editor::putLong)

fun SharedPreferences.int(prefsKey: String, defaultValue: Int = 0) =
        prefsDelegate(prefsKey, defaultValue, this::getInt, Editor::putInt)

fun SharedPreferences.float(prefsKey: String, defaultValue: Float = 0f) =
        prefsDelegate(prefsKey, defaultValue, this::getFloat, Editor::putFloat)

fun SharedPreferences.string(prefsKey: String, defaultValue: String? = null) =
        prefsDelegate(prefsKey, defaultValue, this::getString, Editor::putString)

fun SharedPreferences.stringSet(prefsKey: String, defaultValue: Set<String>? = null) =
        prefsDelegate(prefsKey, defaultValue, this::getStringSet, Editor::putStringSet)


private fun <T> SharedPreferences.prefsDelegate(
        prefsKey: String,
        defaultValue: T,
        readFunc: (String, T) -> T,
        writeFunc: Editor.(String, T) -> Editor
): ReadWriteProperty<Any, T> = object : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = readFunc(prefsKey, defaultValue)
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = this@prefsDelegate.edit().writeFunc(prefsKey, value).apply()
}
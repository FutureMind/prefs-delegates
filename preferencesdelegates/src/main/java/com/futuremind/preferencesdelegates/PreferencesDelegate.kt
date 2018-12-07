package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KFunction2
import kotlin.reflect.KProperty

fun SharedPreferences.boolean(prefsKey: String, defaultValue: Boolean) =
        prefsDelegate(prefsKey, defaultValue, this::getBoolean, Editor::putBoolean)


private fun <T> SharedPreferences.prefsDelegate(prefsKey: String, defaultValue: T, readFunc: (String, T) -> T, writeFunc: Editor.(String, T) -> Editor): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
                readFunc(prefsKey, defaultValue)
        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
                this@prefsDelegate.edit().writeFunc(prefsKey, value).apply()
    }
}
//
//inline fun <reified T> SharedPreferences.prefsDelegate(prefsKey: String, defaultValue: T): ReadWriteProperty<Any, T> {
//    return object : ReadWriteProperty<Any, T> {
//        override fun getValue(thisRef: Any, property: KProperty<*>): T =
//                when (T::class) {
//                    String::class -> this@prefsDelegate.getString(prefsKey, (defaultValue as String)) as T
//                            ?: defaultValue as T
//                    Long::class -> this@prefsDelegate.getLong(prefsKey, defaultValue as Long) as T
//                    Int::class -> this@prefsDelegate.getInt(prefsKey, defaultValue as Int) as T
//                    Float::class -> this@prefsDelegate.getFloat(prefsKey, defaultValue as Float) as T
//                    Boolean::class -> this@prefsDelegate.getBoolean(prefsKey, defaultValue as Boolean) as T
//                    else -> throw IllegalAccessException("Shared preferences don't support type ${T::class.java.simpleName}")
//                }
//
//        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
//            when (T::class) {
//                String::class -> this@prefsDelegate.edit().putString(prefsKey, value as String).apply()
//                Long::class -> this@prefsDelegate.edit().putLong(prefsKey, value as Long).apply()
//                Int::class -> this@prefsDelegate.edit().putInt(prefsKey, value as Int).apply()
//                Float::class -> this@prefsDelegate.edit().putFloat(prefsKey, value as Float).apply()
//                Boolean::class -> this@prefsDelegate.edit().putBoolean(prefsKey, value as Boolean).apply()
//                else -> throw IllegalAccessException("Shared preferences don't support type ${T::class.java.simpleName}")
//            }
//        }
//    }
//}

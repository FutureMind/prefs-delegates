package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> SharedPreferences.boolean(key: String, onSetValue: (value: Boolean) -> Unit, defaultValue: Boolean = false): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return getBoolean(key, defaultValue) as T
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            edit().putBoolean(key, value as Boolean).apply()
            onSetValue(value)
        }
    }
}

fun <T> SharedPreferences.string(key: String, onSetValue: (value: String) -> Unit, defaultValue: String = ""): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return (getString(key, defaultValue) ?: defaultValue) as T
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            edit().putString(key, value as String).apply()
            onSetValue(value)
        }
    }
}

fun <T> SharedPreferences.float(key: String, onSetValue: (value: Float) -> Unit, defaultValue: Float = -1.0f): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return getFloat(key, defaultValue) as T
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            edit().putFloat(key, value as Float).apply()
            onSetValue(value)
        }
    }
}

fun <T> SharedPreferences.integer(key: String, onSetValue: (value: Int) -> Unit, defaultValue: Int = -1): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return getInt(key, defaultValue) as T
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            edit().putInt(key, value as Int).apply()
            onSetValue(value)
        }
    }
}

fun <T> SharedPreferences.long(key: String, onSetValue: (value: Long) -> Unit, defaultValue: Long = -1L): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return getLong(key, defaultValue) as T
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            edit().putLong(key, value as Long).apply()
            onSetValue(value)
        }
    }
}
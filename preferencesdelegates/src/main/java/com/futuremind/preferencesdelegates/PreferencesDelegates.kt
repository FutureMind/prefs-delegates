package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun SharedPreferences.boolean(key: String, defaultValue: Boolean = false): ReadWriteProperty<Any, Boolean> {
    return object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return getBoolean(key, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            edit().putBoolean(key, value).apply()
        }
    }
}

fun SharedPreferences.string(key: String, defaultValue: String = ""): ReadWriteProperty<Any, String> {
    return object : ReadWriteProperty<Any, String> {
        override fun getValue(thisRef: Any, property: KProperty<*>): String {
            return (getString(key, defaultValue) ?: defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
            edit().putString(key, value).apply()
        }
    }
}

fun SharedPreferences.float(key: String, defaultValue: Float = -1.0f): ReadWriteProperty<Any, Float> {
    return object : ReadWriteProperty<Any, Float> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float {
            return getFloat(key, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
            edit().putFloat(key, value).apply()

        }
    }
}

fun SharedPreferences.integer(key: String, defaultValue: Int = -1): ReadWriteProperty<Any, Int> {
    return object : ReadWriteProperty<Any, Int> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return getInt(key, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
            edit().putInt(key, value).apply()

        }
    }
}

fun SharedPreferences.long(key: String,defaultValue: Long = -1L): ReadWriteProperty<Any, Long> {
    return object : ReadWriteProperty<Any, Long> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Long {
            return getLong(key, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
            edit().putLong(key, value).apply()
        }
    }
}
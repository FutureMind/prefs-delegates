package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/*fun SharedPreferences.boolean(key: String, defaultValue: Boolean = false): ReadWriteProperty<Any, Boolean> {
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
}*/



inline fun <reified T> SharedPreferences.prefsDelegate(key: String, defaultValue: T): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return getValueBasedOnType(key, this@prefsDelegate, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            putValueBasedOnType(key, value, this@prefsDelegate)
        }
    }
}

inline fun <reified T> getValueBasedOnType(prefsKey: String, sharedPreferences: SharedPreferences, defaultValue: T): T =
        when (T::class) {
            String::class -> sharedPreferences.getString(prefsKey, (defaultValue as String)) as T
                    ?: defaultValue as T
            Long::class -> sharedPreferences.getLong(prefsKey, defaultValue as Long) as T
            Int::class -> sharedPreferences.getInt(prefsKey, defaultValue as Int) as T
            Float::class -> sharedPreferences.getFloat(prefsKey, defaultValue as Float) as T
            Boolean::class -> sharedPreferences.getBoolean(prefsKey, defaultValue as Boolean) as T
            else -> throw IllegalAccessException("Shared preferences don't support type ${T::class.java.simpleName}")
        }

inline fun <reified T> putValueBasedOnType(prefsKey: String, value: T, sharedPreferences: SharedPreferences) =
        when (T::class) {
            String::class -> sharedPreferences.edit().putString(prefsKey, value as String).apply()
            Long::class -> sharedPreferences.edit().putLong(prefsKey, value as Long).apply()
            Int::class -> sharedPreferences.edit().putInt(prefsKey, value as Int).apply()
            Float::class -> sharedPreferences.edit().putFloat(prefsKey, value as Float).apply()
            Boolean::class -> sharedPreferences.edit().putBoolean(prefsKey, value as Boolean).apply()
            else -> throw IllegalAccessException("Shared preferences don't support type ${T::class.java.simpleName}")
        }
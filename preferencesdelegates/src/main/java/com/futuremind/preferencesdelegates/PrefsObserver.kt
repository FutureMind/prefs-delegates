package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.processors.BehaviorProcessor
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefsObserver<T> constructor(val prefsKey: String, val sharedPreferences: SharedPreferences, val clazz: Class<T>, val defaultValue: T) {

    private val processor = BehaviorProcessor.create<T>()

    fun observe(): Observable<T> = processor.toObservable().share()

    fun saveAndGetValue(value: T): Single<T> {
        putValue(prefsKey, value)
        processor.onNext(value)
        return Single.fromCallable { value }
    }

    fun save(value: T): Completable {
        putValue(prefsKey, value)
        return Completable.complete()
    }

    private fun putValue(prefsKey: String, value: T) =
            when (clazz::class.java) {
                String::class.java -> sharedPreferences.edit().putString(prefsKey, value as String).commit()
                Long::class.java -> sharedPreferences.edit().putLong(prefsKey, value as Long).commit()
                Int::class.java -> sharedPreferences.edit().putInt(prefsKey, value as Int).commit()
                Float::class.java -> sharedPreferences.edit().putFloat(prefsKey, value as Float).commit()
                Boolean::class.java -> sharedPreferences.edit().putBoolean(prefsKey, value as Boolean).commit()
                else -> throw IllegalAccessException("Shared preferences don't support type ${clazz.simpleName}")
            }

    private fun getValue() =
            when (clazz::class.java) {
                String::class.java -> sharedPreferences.getString(prefsKey, (defaultValue as String)) ?: defaultValue
                Long::class.java -> sharedPreferences.getLong(prefsKey, defaultValue as Long)
                Int::class.java -> sharedPreferences.getInt(prefsKey, defaultValue as Int)
                Float::class.java -> sharedPreferences.getFloat(prefsKey, defaultValue as Float)
                Boolean::class.java -> sharedPreferences.getBoolean(prefsKey, defaultValue as Boolean)
                else -> throw IllegalAccessException("Shared preferences don't support type ${clazz.simpleName}")
            }

}


inline fun <reified T> SharedPreferences.valueDelegate(key: String, noinline onSetValue: (value: T) -> Unit, defaultValue: T): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return getValueBasedOnType(key, this@valueDelegate, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            putValueBasedOnType(key, value, this@valueDelegate)
            onSetValue(value)
        }
    }
}

inline fun <reified T> getValueBasedOnType(prefsKey: String, sharedPreferences: SharedPreferences, defaultValue: T): T =
        when (T::class.java) {
            String::class.java -> sharedPreferences.getString(prefsKey, (defaultValue as String)) as T
                    ?: defaultValue
            else -> sharedPreferences.getString(prefsKey, (defaultValue as String)) as T
                    ?: defaultValue
        }

inline fun <reified T> putValueBasedOnType(prefsKey: String, value: T, sharedPreferences: SharedPreferences) =
        when (T::class.java) {
            String::class.java -> sharedPreferences.edit().putString(prefsKey, value as String).apply()
            else -> {
            }
        }



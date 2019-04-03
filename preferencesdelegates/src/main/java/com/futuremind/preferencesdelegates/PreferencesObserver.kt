package com.futuremind.preferencesdelegates

import android.annotation.SuppressLint
import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.processors.BehaviorProcessor

class PreferencesObserver<T> constructor(
        private val prefsKey: String,
        private val sharedPreferences: SharedPreferences,
        private val clazz: Class<T>,
        private val defaultValue: T
) {

    private val processor = BehaviorProcessor.create<T>()

    fun observe(): Observable<T> = processor.toObservable().share()

    fun saveAndGetValue(value: T): Single<T> =
            Single.fromCallable { putValue(prefsKey, value) }
                    .map { it.commit() }
                    .doOnSuccess { processor.onNext(value) }
                    .map { value }

    fun saveValue(value: T): Completable = saveAndGetValue(value)
            .flatMapCompletable { Completable.complete() }

    fun saveValueWithoutCommit(value: T): Completable =
            Single.fromCallable { putValue(prefsKey, value) }
                    .doOnSuccess { processor.onNext(value) }
                    .flatMapCompletable { Completable.complete() }

    @SuppressLint("ApplySharedPref")
    fun commitChanges(): Completable = Completable.fromCallable { sharedPreferences.edit().commit() }


    private fun putValue(prefsKey: String, value: T) =
            when (clazz) {
                String::class.java -> sharedPreferences.edit().putString(prefsKey, value as String)
                Long::class.java -> sharedPreferences.edit().putLong(prefsKey, value as Long)
                Int::class.java -> sharedPreferences.edit().putInt(prefsKey, value as Int)
                Float::class.java -> sharedPreferences.edit().putFloat(prefsKey, value as Float)
                Boolean::class.java -> sharedPreferences.edit().putBoolean(prefsKey, value as Boolean)
                else -> throw IllegalAccessException("Shared preferences don't support type ${clazz.simpleName}")
            }


    fun getValue(): Single<T> = Single.fromCallable {
        when (clazz) {
            String::class.java -> sharedPreferences.getString(prefsKey, (defaultValue as String)) as T
                    ?: defaultValue as T
            Long::class.java -> sharedPreferences.getLong(prefsKey, defaultValue as Long) as T
            Int::class.java -> sharedPreferences.getInt(prefsKey, defaultValue as Int) as T
            Float::class.java -> sharedPreferences.getFloat(prefsKey, defaultValue as Float) as T
            Boolean::class.java -> sharedPreferences.getBoolean(prefsKey, defaultValue as Boolean) as T
            else -> throw IllegalAccessException("Shared preferences don't support type ${clazz.simpleName}")
        }
    }

}
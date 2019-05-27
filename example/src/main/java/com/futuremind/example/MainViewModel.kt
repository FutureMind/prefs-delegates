package com.futuremind.example

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import javax.inject.Inject

class MainViewModel @Inject constructor(private val testStore: TestStore) : ViewModel() {

    fun saveAge(age: Int){
        testStore.age = age
    }

    fun saveToken(token: String) {
        testStore.token.save(token)
    }

    fun saveEnum(enum: TestStore.SomeEnum) {
        testStore.enum = enum
    }

    fun savePerson(person: Person) {
        testStore.person = person
    }

    fun getEnum() = testStore.enum

    fun getPerson(): Person? = testStore.person

    fun observeTokenChange(): Observable<String> = testStore.token.observable()
}

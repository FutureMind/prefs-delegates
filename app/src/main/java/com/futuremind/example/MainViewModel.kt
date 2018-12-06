package com.futuremind.example

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class MainViewModel @Inject constructor(private val testStore: TestStore) : ViewModel() {

    fun saveAge(age: Int){
        testStore.age = age
    }
    fun saveToken(token: String) {
        testStore.token.saveAndCommit(token).subscribe()
    }

    fun observeTokenChange(): Observable<String> = testStore.token.observe()

}
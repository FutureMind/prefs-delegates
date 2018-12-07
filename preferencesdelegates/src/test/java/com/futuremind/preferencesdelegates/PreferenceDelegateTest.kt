package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PreferenceDelegateTest {

    @Mock lateinit var prefs: SharedPreferences
    @Mock lateinit var prefsEditor: SharedPreferences.Editor

    private lateinit var prefsContainer: PrefValuesContainer

    @Before
    fun setUp(){
        whenever(prefs.edit()).thenReturn(prefsEditor)
        whenever(prefsEditor.putInt(any(), any())).thenReturn(prefsEditor)
        prefsContainer = PrefValuesContainer(prefs)
    }


    @Test
    fun abc(){

        prefsContainer.intValue = 12

        verify(prefsEditor).putInt("the int", 12)

    }

    class PrefValuesContainer(prefs: SharedPreferences){

        var intValue by prefs.int("the int", 0)

    }

}
package com.futuremind.preferencesdelegates.rx

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock

@RunWith(MockitoJUnitRunner::class)
class RxPreferencesDelegatesTest {

    companion object {
        val INT = "INT"
    }

    class PrefsValuesContainer(prefs: SharedPreferences) {
        var intValue by prefs.rxPrefsDelegate(INT, 0)
    }

    @Mock private lateinit var prefs: SharedPreferences
    @Mock private lateinit var prefsEditor: SharedPreferences.Editor
    private lateinit var prefsContainer: PrefsValuesContainer

    @Before
    fun setUp() {
        whenever(prefs.edit()).thenReturn(prefsEditor)
        whenever(prefsEditor.putInt(any(), any())).thenReturn(prefsEditor)
        prefsContainer = PrefsValuesContainer(prefs)
    }

    @Test
    fun testIntReadSave() {
        var result = 0
        prefsContainer.intValue = 21.justIt()
        prefsContainer.intValue.subscribe { result = it }
        assertEquals(21, result)
        verify(prefsEditor).putInt(INT, 21)
    }
}
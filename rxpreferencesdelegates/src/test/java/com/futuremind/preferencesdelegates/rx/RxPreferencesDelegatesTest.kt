package com.futuremind.preferencesdelegates.rx

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RxPreferencesDelegatesTest {

    companion object {
        val INT = "INT"
    }

    class PrefsValuesContainer(prefs: SharedPreferences) {
        var intValue by prefs.observableInt(INT)
        val intObservable get() = ::intValue.asObservable()
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
    fun `when save item then subscriber gets it`() {
        var result = 0
        prefsContainer.intObservable.subscribe { result = it }
        prefsContainer.intValue = 21

        assertEquals(21, result)
        verify(prefsEditor).putInt(INT, 21)
    }

    @Test
    fun `when subscribe after saving item then subscriber gets it`() {
        var result = 0
        prefsContainer.intValue = 21
        prefsContainer.intObservable.subscribe { result = it }

        assertEquals(21, result)
        verify(prefsEditor).putInt(INT, 21)
    }

    @Test
    fun `when save item twice then subscriber gets correct values`() {
        val results = mutableListOf<Int>()
        prefsContainer.intValue = 21
        prefsContainer.intObservable.subscribe { results += it }
        prefsContainer.intValue = 58

        assertEquals(21, results[0])
        assertEquals(58, results[1])
        verify(prefsEditor).putInt(INT, 21)
    }

    @Test
    fun `when subscribe before saving item twice then subscriber gets correct values including default`() {
        val results = mutableListOf<Int>()
        prefsContainer.intObservable.subscribe { results += it }
        prefsContainer.intValue = 21
        prefsContainer.intValue = 58

        assertEquals(0, results[0])
        assertEquals(21, results[1])
        assertEquals(58, results[2])
        verify(prefsEditor).putInt(INT, 21)
    }
}
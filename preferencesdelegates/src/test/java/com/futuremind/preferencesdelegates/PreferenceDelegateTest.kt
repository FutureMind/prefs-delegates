package com.futuremind.preferencesdelegates

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
class PreferenceDelegateTest {

    companion object {
        private const val BOOLEAN = "BOOLEAN"
        private const val INT = "INT"
        private const val LONG = "LONG"
        private const val FLOAT = "FLOAT"
        private const val STRING = "STRING"
        private const val STRING_SET = "STRING_SET"
    }

    class PrefValuesContainer(prefs: SharedPreferences) {
        var booleanValue by prefs.boolean(BOOLEAN)
        var intValue by prefs.int(INT)
        var longValue by prefs.long(LONG)
        var floatValue by prefs.float(FLOAT)
        var stringValue by prefs.string(STRING)
        var stringSetValue by prefs.stringSet(STRING_SET)
    }

    @Mock lateinit var prefs: SharedPreferences
    @Mock lateinit var prefsEditor: SharedPreferences.Editor

    private lateinit var prefsContainer: PrefValuesContainer

    @Before
    fun setUp() {
        whenever(prefs.edit()).thenReturn(prefsEditor)
        whenever(prefsEditor.putBoolean(any(), any())).thenReturn(prefsEditor)
        whenever(prefsEditor.putInt(any(), any())).thenReturn(prefsEditor)
        whenever(prefsEditor.putLong(any(), any())).thenReturn(prefsEditor)
        whenever(prefsEditor.putFloat(any(), any())).thenReturn(prefsEditor)
        whenever(prefsEditor.putString(any(), any())).thenReturn(prefsEditor)
        whenever(prefsEditor.putStringSet(any(), any())).thenReturn(prefsEditor)
        prefsContainer = PrefValuesContainer(prefs)
    }

    @Test
    fun testIntSave() {
        prefsContainer.intValue = 12
        verify(prefsEditor).putInt(INT, 12)
    }

    @Test
    fun testIntRead() {
        whenever(prefs.getInt(INT, 0)).thenReturn(13)
        assertEquals(prefsContainer.intValue, 13)
    }

    @Test
    fun testBooleanSave() {
        prefsContainer.booleanValue = true
        verify(prefsEditor).putBoolean(BOOLEAN, true)
    }

    @Test
    fun testBooleanRead() {
        whenever(prefs.getBoolean(BOOLEAN, false)).thenReturn(true)
        assertEquals(prefsContainer.booleanValue, true)
    }

    @Test
    fun testLongSave() {
        prefsContainer.longValue = 17
        verify(prefsEditor).putLong(LONG, 17)
    }

    @Test
    fun testLongRead() {
        whenever(prefs.getLong(LONG, 0)).thenReturn(18)
        assertEquals(prefsContainer.longValue, 18)
    }

    @Test
    fun testFloatSave() {
        prefsContainer.floatValue = 17f
        verify(prefsEditor).putFloat(FLOAT, 17f)
    }

    @Test
    fun testFloatRead() {
        whenever(prefs.getFloat(FLOAT, 0f)).thenReturn(18f)
        assertEquals(prefsContainer.floatValue, 18f)
    }

    @Test
    fun testStringSave() {
        prefsContainer.stringValue = "hey"
        verify(prefsEditor).putString(STRING, "hey")
    }

    @Test
    fun testStringRead() {
        whenever(prefs.getString(STRING, null)).thenReturn("ho")
        assertEquals(prefsContainer.stringValue, "ho")
    }

    @Test
    fun testStringSetSave() {
        prefsContainer.stringSetValue = setOf("hey")
        verify(prefsEditor).putStringSet(STRING_SET, setOf("hey"))
    }

    @Test
    fun testStringSetRead() {
        whenever(prefs.getStringSet(STRING_SET, null)).thenReturn(setOf("ho"))
        assertEquals(prefsContainer.stringSetValue, setOf("ho"))
    }

}
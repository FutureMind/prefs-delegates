package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.squareup.moshi.Moshi
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
        private const val ENUM = "ENUM"
        private const val JSON = "JSON"
        private val moshi = Moshi.Builder().build()
        private val jsonAdapter = moshi.adapter(Person::class.java)
    }

    enum class SomeEnum { DEFAULT, NICE, CRAP }

    data class Person(val name: String, val age: Int)

    class PrefValuesContainer(prefs: SharedPreferences) {
        var booleanValue by prefs.boolean(BOOLEAN)
        var intValue by prefs.int(INT)
        var longValue by prefs.long(LONG)
        var floatValue by prefs.float(FLOAT)
        var stringValue by prefs.string(STRING)
        var stringSetValue by prefs.stringSet(STRING_SET)
        var enumValue by prefs.enum<SomeEnum>(ENUM, SomeEnum.DEFAULT)
        var jsonValue by prefs.json<Person>(JSON, null, moshi)
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
    fun testIntRead() {
        whenever(prefs.getInt(INT, 0)).thenReturn(13)
        assertEquals(13, prefsContainer.intValue)
    }

    @Test
    fun testIntSave() {
        prefsContainer.intValue = 12
        verify(prefsEditor).putInt(INT, 12)
    }

    @Test
    fun testBooleanRead() {
        whenever(prefs.getBoolean(BOOLEAN, false)).thenReturn(true)
        assertEquals(true, prefsContainer.booleanValue)
    }

    @Test
    fun testBooleanSave() {
        prefsContainer.booleanValue = true
        verify(prefsEditor).putBoolean(BOOLEAN, true)
    }

    @Test
    fun testLongRead() {
        whenever(prefs.getLong(LONG, 0)).thenReturn(18)
        assertEquals(18, prefsContainer.longValue)
    }

    @Test
    fun testLongSave() {
        prefsContainer.longValue = 17
        verify(prefsEditor).putLong(LONG, 17)
    }

    @Test
    fun testFloatRead() {
        whenever(prefs.getFloat(FLOAT, 0f)).thenReturn(18f)
        assertEquals(18f, prefsContainer.floatValue)
    }

    @Test
    fun testFloatSave() {
        prefsContainer.floatValue = 17f
        verify(prefsEditor).putFloat(FLOAT, 17f)
    }

    @Test
    fun testStringRead() {
        whenever(prefs.getString(STRING, null)).thenReturn("ho")
        assertEquals("ho", prefsContainer.stringValue)
    }

    @Test
    fun testStringSave() {
        prefsContainer.stringValue = "hey"
        verify(prefsEditor).putString(STRING, "hey")
    }

    @Test
    fun testStringSetRead() {
        whenever(prefs.getStringSet(STRING_SET, null)).thenReturn(setOf("ho"))
        assertEquals(setOf("ho"), prefsContainer.stringSetValue)
    }

    @Test
    fun testStringSetSave() {
        prefsContainer.stringSetValue = setOf("hey")
        verify(prefsEditor).putStringSet(STRING_SET, setOf("hey"))
    }

    @Test
    fun testEnumRead() {
        whenever(prefs.getString(ENUM, SomeEnum.DEFAULT.name)).thenReturn("NICE")
        assertEquals(SomeEnum.NICE, prefsContainer.enumValue)
    }

    @Test
    fun testEnumSave() {
        prefsContainer.enumValue = SomeEnum.NICE
        verify(prefsEditor).putString(ENUM, SomeEnum.NICE.name)
    }

    @Test
    fun testJsonRead() {
        val person = Person("Johny", 22)
        val personJson = jsonAdapter.toJson(person)
        whenever(prefs.getString(JSON, "")).thenReturn(personJson)
        assertEquals(person, prefsContainer.jsonValue)
    }

    @Test
    fun testJsonWrite() {
        val person = Person("Johny", 22)
        val personJson = jsonAdapter.toJson(person)
        prefsContainer.jsonValue = person
        verify(prefsEditor).putString(JSON, personJson)
    }
}
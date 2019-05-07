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
        private const val NULLABLE_BOOLEAN = "NULLABLE_BOOLEAN"
        private const val NULLABLE_INT = "NULLABLE_INT"
        private const val NULLABLE_LONG = "NULLABLE_LONG"
        private const val NULLABLE_FLOAT = "NULLABLE_FLOAT"
        private const val NULLABLE_STRING = "NULLABLE_STRING"
        private const val NULLABLE_STRING_SET = "NULLABLE_STRING_SET"
        //nullable enum makes no sense, so it's omitted
        private const val NULLABLE_JSON = "NULLABLE_JSON"
        private val moshi = Moshi.Builder().build()
        private val jsonAdapter = moshi.adapter(Person::class.java)
    }

    enum class SomeEnum { DEFAULT, NICE, CRAP }

    data class Person(val name: String, val age: Int)

    class PrefValuesContainer(prefs: SharedPreferences) {
        var booleanValue: Boolean by prefs.boolean(BOOLEAN, false)
        var intValue: Int by prefs.int(INT, 0)
        var longValue: Long by prefs.long(LONG, 0L)
        var floatValue: Float by prefs.float(FLOAT, 0f)
        var stringValue: String by prefs.string(STRING, "")
        var stringSetValue: Set<String> by prefs.stringSet(STRING_SET, emptySet())
        var enumValue: SomeEnum by prefs.enum(ENUM, SomeEnum.DEFAULT)
        var jsonValue: Person by prefs.json(JSON, moshi, Person("stranger", 1))

        var nullableBooleanValue: Boolean? by prefs.boolean(NULLABLE_BOOLEAN)
        var nullableIntValue: Int? by prefs.int(NULLABLE_INT)
        var nullableLongValue: Long? by prefs.long(NULLABLE_LONG)
        var nullableFloatValue: Float? by prefs.float(NULLABLE_FLOAT)
        var nullableStringValue: String? by prefs.string(NULLABLE_STRING)
        var nullableStringSetValue: Set<String>? by prefs.stringSet(NULLABLE_STRING_SET)
        var nullableJsonValue: Person? by prefs.json<Person?>(NULLABLE_JSON, moshi)
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
    fun intRead() {
        whenever(prefs.getInt(INT, 0)).thenReturn(13)
        assertEquals(13, prefsContainer.intValue)
    }

    @Test
    fun intSave() {
        prefsContainer.intValue = 12
        verify(prefsEditor).putInt(INT, 12)
    }

    @Test
    fun booleanRead() {
        whenever(prefs.getBoolean(BOOLEAN, false)).thenReturn(true)
        assertEquals(true, prefsContainer.booleanValue)
    }

    @Test
    fun booleanSave() {
        prefsContainer.booleanValue = true
        verify(prefsEditor).putBoolean(BOOLEAN, true)
    }

    @Test
    fun longRead() {
        whenever(prefs.getLong(LONG, 0)).thenReturn(18)
        assertEquals(18, prefsContainer.longValue)
    }

    @Test
    fun longSave() {
        prefsContainer.longValue = 17
        verify(prefsEditor).putLong(LONG, 17)
    }

    @Test
    fun floatRead() {
        whenever(prefs.getFloat(FLOAT, 0f)).thenReturn(18f)
        assertEquals(18f, prefsContainer.floatValue)
    }

    @Test
    fun floatSave() {
        prefsContainer.floatValue = 17f
        verify(prefsEditor).putFloat(FLOAT, 17f)
    }

    @Test
    fun stringRead() {
        whenever(prefs.getString(STRING, "")).thenReturn("ho")
        assertEquals("ho", prefsContainer.stringValue)
    }

    @Test
    fun stringSave() {
        prefsContainer.stringValue = "hey"
        verify(prefsEditor).putString(STRING, "hey")
    }

    @Test
    fun stringSetRead() {
        whenever(prefs.getStringSet(STRING_SET, emptySet())).thenReturn(setOf("ho"))
        assertEquals(setOf("ho"), prefsContainer.stringSetValue)
    }

    @Test
    fun stringSetSave() {
        prefsContainer.stringSetValue = setOf("hey")
        verify(prefsEditor).putStringSet(STRING_SET, setOf("hey"))
    }

    @Test
    fun enumRead() {
        whenever(prefs.getString(ENUM, SomeEnum.DEFAULT.name)).thenReturn(SomeEnum.NICE.name)
        assertEquals(SomeEnum.NICE, prefsContainer.enumValue)
    }

    @Test
    fun enumSave() {
        prefsContainer.enumValue = SomeEnum.NICE
        verify(prefsEditor).putString(ENUM, SomeEnum.NICE.name)
    }

    @Test
    fun jsonRead() {
        val person = Person("Johny", 22)
        val personJson = jsonAdapter.toJson(person)
        val defaultPerson = Person("stranger", 1)
        whenever(prefs.getString(JSON, jsonAdapter.toJson(defaultPerson))).thenReturn(personJson)
        assertEquals(person, prefsContainer.jsonValue)
    }

    @Test
    fun jsonWrite() {
        val person = Person("Johny", 22)
        val personJson = jsonAdapter.toJson(person)
        prefsContainer.jsonValue = person
        verify(prefsEditor).putString(JSON, personJson)
    }

    @Test
    fun nullableBooleanRead() {
        whenever(prefs.contains(NULLABLE_BOOLEAN)).thenReturn(true)
        whenever(prefs.getBoolean(NULLABLE_BOOLEAN, false)).thenReturn(true)
        assertEquals(true, prefsContainer.nullableBooleanValue)
    }

    @Test
    fun nullableBooleanReadNull() {
        whenever(prefs.contains(NULLABLE_BOOLEAN)).thenReturn(false)
        assertEquals(null, prefsContainer.nullableBooleanValue)
    }

    @Test
    fun nullableBooleanWrite() {
        prefsContainer.nullableBooleanValue = true
        verify(prefsEditor).putBoolean(NULLABLE_BOOLEAN, true)
    }

    @Test
    fun nullableBooleanWriteNull() {
        prefsContainer.nullableBooleanValue = null
        verify(prefsEditor).remove(NULLABLE_BOOLEAN)
    }

    @Test
    fun nullableIntRead() {
        whenever(prefs.contains(NULLABLE_INT)).thenReturn(true)
        whenever(prefs.getInt(NULLABLE_INT, 0)).thenReturn(21)
        assertEquals(21, prefsContainer.nullableIntValue)
    }

    @Test
    fun nullableIntReadNull() {
        whenever(prefs.contains(NULLABLE_INT)).thenReturn(false)
        assertEquals(null, prefsContainer.nullableIntValue)
    }

    @Test
    fun nullableIntWrite() {
        prefsContainer.nullableIntValue = 21
        verify(prefsEditor).putInt(NULLABLE_INT, 21)
    }

    @Test
    fun nullableIntWriteNull() {
        prefsContainer.nullableIntValue = null
        verify(prefsEditor).remove(NULLABLE_INT)
    }

    @Test
    fun nullableLongRead() {
        whenever(prefs.contains(NULLABLE_LONG)).thenReturn(true)
        whenever(prefs.getLong(NULLABLE_LONG, 0)).thenReturn(21L)
        assertEquals(21L, prefsContainer.nullableLongValue)
    }

    @Test
    fun nullableLongReadNull() {
        whenever(prefs.contains(NULLABLE_LONG)).thenReturn(false)
        assertEquals(null, prefsContainer.nullableLongValue)
    }

    @Test
    fun nullableLongWrite() {
        prefsContainer.nullableLongValue = 21L
        verify(prefsEditor).putLong(NULLABLE_LONG, 21L)
    }

    @Test
    fun nullableLongWriteNull() {
        prefsContainer.nullableLongValue = null
        verify(prefsEditor).remove(NULLABLE_LONG)
    }

    @Test
    fun nullableFloatRead() {
        whenever(prefs.contains(NULLABLE_FLOAT)).thenReturn(true)
        whenever(prefs.getFloat(NULLABLE_FLOAT, 0f)).thenReturn(21f)
        assertEquals(21f, prefsContainer.nullableFloatValue)
    }

    @Test
    fun nullableFloatReadNull() {
        whenever(prefs.contains(NULLABLE_FLOAT)).thenReturn(false)
        assertEquals(null, prefsContainer.nullableFloatValue)
    }

    @Test
    fun nullableFloatWrite() {
        prefsContainer.nullableFloatValue= 21f
        verify(prefsEditor).putFloat(NULLABLE_FLOAT, 21f)
    }

    @Test
    fun nullableFloatWriteNull() {
        prefsContainer.nullableFloatValue = null
        verify(prefsEditor).remove(NULLABLE_FLOAT)
    }

    @Test
    fun nullableStringRead() {
        whenever(prefs.contains(NULLABLE_STRING)).thenReturn(true)
        whenever(prefs.getString(NULLABLE_STRING, "")).thenReturn("hey")
        assertEquals("hey", prefsContainer.nullableStringValue)
    }

    @Test
    fun nullableStringReadNull() {
        whenever(prefs.contains(NULLABLE_STRING)).thenReturn(false)
        assertEquals(null, prefsContainer.nullableStringValue)
    }

    @Test
    fun nullableStringWrite() {
        prefsContainer.nullableStringValue = "hey"
        verify(prefsEditor).putString(NULLABLE_STRING, "hey")
    }

    @Test
    fun nullableStringWriteNull() {
        prefsContainer.nullableStringValue = null
        verify(prefsEditor).remove(NULLABLE_STRING)
    }

    @Test
    fun nullableStringSetRead() {
        whenever(prefs.contains(NULLABLE_STRING_SET)).thenReturn(true)
        whenever(prefs.getStringSet(NULLABLE_STRING_SET, emptySet())).thenReturn(setOf("hey", "ho"))
        assertEquals(setOf("hey", "ho"), prefsContainer.nullableStringSetValue)
    }

    @Test
    fun nullableStringSetReadNull() {
        whenever(prefs.contains(NULLABLE_STRING_SET)).thenReturn(false)
        assertEquals(null, prefsContainer.nullableStringSetValue)
    }

    @Test
    fun nullableStringSetWrite() {
        prefsContainer.nullableStringSetValue = setOf("hey", "ho")
        verify(prefsEditor).putStringSet(NULLABLE_STRING_SET, setOf("hey", "ho"))
    }

    @Test
    fun nullableStringSetWriteNull() {
        prefsContainer.nullableStringSetValue = null
        verify(prefsEditor).remove(NULLABLE_STRING_SET)
    }

    @Test
    fun nullableJsonRead() {
        val person = Person("Johny", 22)
        val personJson = jsonAdapter.toJson(person)
        whenever(prefs.getString(NULLABLE_JSON, "")).thenReturn(personJson)
        assertEquals(person, prefsContainer.nullableJsonValue)
    }

    @Test
    fun nullableJsonReadNull() {
        assertEquals(null, prefsContainer.nullableJsonValue)
    }

    @Test
    fun nullableJsonWrite() {
        val person = Person("Johny", 22)
        val personJson = jsonAdapter.toJson(person)
        prefsContainer.nullableJsonValue = person
        verify(prefsEditor).putString(NULLABLE_JSON, personJson)
    }

    @Test
    fun nullableJsonWriteNull() {
        prefsContainer.nullableJsonValue = null
        verify(prefsEditor).remove(NULLABLE_JSON)
    }
}

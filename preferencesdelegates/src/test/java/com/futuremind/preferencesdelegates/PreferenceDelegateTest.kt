package com.futuremind.preferencesdelegates

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PreferenceDelegateTest {

    companion object {
        private const val BOOLEAN_IMPLICIT = "BOOLEAN_IMPLICIT"
        private const val BOOLEAN_EXPLICIT = "BOOLEAN_EXPLICIT"
        private const val BOOLEAN_NULLABLE_IMPLICIT = "BOOLEAN_NULLABLE_IMPLICIT"
        private const val BOOLEAN_NULLABLE_EXPLICIT = "BOOLEAN_NULLABLE_EXPLICIT"
        private const val BOOLEAN_NULLABLE_EXPLICIT_DEFAULT = "BOOLEAN_NULLABLE_EXPLICIT_DEFAULT"
        private const val BOOLEAN_NULLABLE_EXPLICIT_NULL = "BOOLEAN_NULLABLE_EXPLICIT_NULL"
        private const val INT = "INT"
        private const val INT_NULLABLE = "INT_NULLABLE"
        private const val LONG = "LONG"
        private const val LONG_NULLABLE = "LONG_NULLABLE"
        private const val FLOAT = "FLOAT"
        private const val FLOAT_NULLABLE = "FLOAT_NULLABLE"
        private const val STRING = "STRING"
        private const val STRING_NULLABLE = "STRING_NULLABLE"
        private const val STRING_SET = "STRING_SET"
        private const val STRING_SET_NULLABLE = "STRING_SET_NULLABLE"
        private const val ENUM = "ENUM"
        private const val ENUM_NULLABLE = "ENUM_NULLABLE"
        private const val JSON = "JSON"
        private const val JSON_NULLABLE = "JSON_NULLABLE"

        private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        private val jsonAdapter = moshi.adapter(Person::class.java)
        private val jsonOldFormatAdapter = moshi.adapter(PersonOldFormat::class.java)
    }

    enum class SomeEnum { DEFAULT, NICE, CRAP }

    data class Person(val name: String, val age: Int)
    data class PersonOldFormat(val name: String)

    class PrefValuesContainer(prefs: SharedPreferences) {
        var booleanValueImplicit by prefs.boolean(BOOLEAN_IMPLICIT, false)
        var booleanValueExplicit: Boolean by prefs.boolean(BOOLEAN_EXPLICIT, false)
        var booleanValueNullableImplicit by prefs.boolean(BOOLEAN_NULLABLE_IMPLICIT)
        var booleanValueNullableExplicit: Boolean? by prefs.boolean(BOOLEAN_NULLABLE_EXPLICIT)
        var booleanValueNullableExplicitDefault: Boolean? by prefs.boolean(BOOLEAN_NULLABLE_EXPLICIT_DEFAULT, false)
        var booleanValueNullableExplicitNull: Boolean? by prefs.boolean(BOOLEAN_NULLABLE_EXPLICIT_NULL, null)
        var intValue by prefs.int(INT, 1)
        var intValueNullable by prefs.int(INT_NULLABLE)
        var longValue by prefs.long(LONG, 1L)
        var longValueNullable by prefs.long(LONG_NULLABLE)
        var floatValue by prefs.float(FLOAT, 1f)
        var floatValueNullable by prefs.float(FLOAT_NULLABLE)
        var stringValue by prefs.string(STRING, "a")
        var stringValueNullable by prefs.string(STRING_NULLABLE)
        var stringSetValue by prefs.stringSet(STRING_SET, setOf("a"))
        var stringSetValueNullable by prefs.stringSet(STRING_SET_NULLABLE)
        var enumValue: SomeEnum by prefs.enum(ENUM, SomeEnum.DEFAULT)
        var enumValueNullable: SomeEnum? by prefs.enum(ENUM_NULLABLE)
        var jsonValue: Person by prefs.json(JSON, moshi, Person("a", 1))
        var jsonValueNullable: Person? by prefs.json(JSON_NULLABLE, moshi)
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
    fun booleanImplicitReadDefault() {
        whenever(prefs.contains(BOOLEAN_IMPLICIT)).thenReturn(false)
        assertFalse(prefsContainer.booleanValueImplicit)
    }

    @Test
    fun booleanImplicitRead() {
        whenever(prefs.contains(BOOLEAN_IMPLICIT)).thenReturn(true)
        whenever(prefs.getBoolean(BOOLEAN_IMPLICIT, false)).thenReturn(true)
        assertTrue(prefsContainer.booleanValueImplicit)
    }

    @Test
    fun booleanImplicitSave() {
        prefsContainer.booleanValueImplicit = true
        verify(prefsEditor).putBoolean(BOOLEAN_IMPLICIT, true)
    }

    @Test
    fun booleanExplicitRead() {
        whenever(prefs.contains(BOOLEAN_EXPLICIT)).thenReturn(true)
        whenever(prefs.getBoolean(BOOLEAN_EXPLICIT, false)).thenReturn(true)
        assertTrue(prefsContainer.booleanValueExplicit)
    }

    @Test
    fun booleanExplicitSave() {
        prefsContainer.booleanValueExplicit = true
        verify(prefsEditor).putBoolean(BOOLEAN_EXPLICIT, true)
    }

    @Test
    fun booleanNullableImplicitReadDefault() {
        whenever(prefs.contains(BOOLEAN_NULLABLE_IMPLICIT)).thenReturn(false)
        assertNull(prefsContainer.booleanValueNullableImplicit)
    }

    @Test
    fun booleanNullableImplicitRead() {
        whenever(prefs.contains(BOOLEAN_NULLABLE_IMPLICIT)).thenReturn(true)
        whenever(prefs.getBoolean(BOOLEAN_NULLABLE_IMPLICIT, false)).thenReturn(true)
        assertEquals(true, prefsContainer.booleanValueNullableImplicit)
    }

    @Test
    fun booleanNullableImplicitSave() {
        prefsContainer.booleanValueNullableImplicit = true
        verify(prefsEditor).putBoolean(BOOLEAN_NULLABLE_IMPLICIT, true)
    }

    @Test
    fun booleanNullableImplicitSaveNull() {
        prefsContainer.booleanValueNullableImplicit = null
        verify(prefsEditor).remove(BOOLEAN_NULLABLE_IMPLICIT)
    }

    @Test
    fun booleanNullableExplicitRead() {
        whenever(prefs.contains(BOOLEAN_NULLABLE_EXPLICIT)).thenReturn(true)
        whenever(prefs.getBoolean(BOOLEAN_NULLABLE_EXPLICIT, false)).thenReturn(true)
        assertEquals(true, prefsContainer.booleanValueNullableExplicit)
    }

    @Test
    fun booleanNullableExplicitSave() {
        prefsContainer.booleanValueNullableExplicit = true
        verify(prefsEditor).putBoolean(BOOLEAN_NULLABLE_EXPLICIT, true)
    }

    @Test
    fun booleanNullableExplicitSaveNull() {
        prefsContainer.booleanValueNullableExplicit = null
        verify(prefsEditor).remove(BOOLEAN_NULLABLE_EXPLICIT)
    }

    @Test
    fun booleanNullableExplicitDefaultReadDefault() {
        whenever(prefs.contains(BOOLEAN_NULLABLE_EXPLICIT_DEFAULT)).thenReturn(false)
        assertEquals(false, prefsContainer.booleanValueNullableExplicitDefault)
    }

    fun booleanNullableExplicitDefaultRead() {
        whenever(prefs.contains(BOOLEAN_NULLABLE_EXPLICIT_DEFAULT)).thenReturn(true)
        whenever(prefs.getBoolean(BOOLEAN_NULLABLE_EXPLICIT_DEFAULT, false)).thenReturn(true)
        assertEquals(true, prefsContainer.booleanValueNullableExplicitDefault)
    }

    @Test
    fun booleanNullableExplicitDefaultSave() {
        prefsContainer.booleanValueNullableExplicitDefault = true
        verify(prefsEditor).putBoolean(BOOLEAN_NULLABLE_EXPLICIT_DEFAULT, true)
    }

    @Test
    fun booleanNullableExplicitDefaultSaveNull() {
        prefsContainer.booleanValueNullableExplicitDefault = null
        verify(prefsEditor).remove(BOOLEAN_NULLABLE_EXPLICIT_DEFAULT)
    }

    @Test
    fun booleanNullableExplicitNullReadDefault() {
        whenever(prefs.contains(BOOLEAN_NULLABLE_EXPLICIT_NULL)).thenReturn(false)
        assertNull(prefsContainer.booleanValueNullableExplicitNull)
    }

    @Test
    fun booleanNullableExplicitNullRead() {
        whenever(prefs.contains(BOOLEAN_NULLABLE_EXPLICIT_NULL)).thenReturn(true)
        whenever(prefs.getBoolean(BOOLEAN_NULLABLE_EXPLICIT_NULL, false)).thenReturn(true)
        assertEquals(true, prefsContainer.booleanValueNullableExplicitNull)
    }

    @Test
    fun booleanNullableExplicitNullSave() {
        prefsContainer.booleanValueNullableExplicitNull = true
        verify(prefsEditor).putBoolean(BOOLEAN_NULLABLE_EXPLICIT_NULL, true)
    }

    @Test
    fun booleanNullableExplicitNullSaveNull() {
        prefsContainer.booleanValueNullableExplicitNull = null
        verify(prefsEditor).remove(BOOLEAN_NULLABLE_EXPLICIT_NULL)
    }


    @Test
    fun intReadDefault() {
        whenever(prefs.contains(INT)).thenReturn(false)
        assertEquals(1, prefsContainer.intValue)
    }

    @Test
    fun intRead() {
        whenever(prefs.contains(INT)).thenReturn(true)
        whenever(prefs.getInt(INT, 0)).thenReturn(2)
        assertEquals(2, prefsContainer.intValue)
    }

    @Test
    fun intSave() {
        prefsContainer.intValue = 3
        verify(prefsEditor).putInt(INT, 3)
    }

    @Test
    fun intNullableReadDefault() {
        whenever(prefs.contains(INT_NULLABLE)).thenReturn(false)
        assertNull(prefsContainer.intValueNullable)
    }

    @Test
    fun intNullableRead() {
        whenever(prefs.contains(INT_NULLABLE)).thenReturn(true)
        whenever(prefs.getInt(INT_NULLABLE, 0)).thenReturn(2)
        assertEquals(2, prefsContainer.intValueNullable)
    }

    @Test
    fun intNullableSave() {
        prefsContainer.intValueNullable = 3
        verify(prefsEditor).putInt(INT_NULLABLE, 3)
    }

    @Test
    fun nullableIntSaveNull() {
        prefsContainer.intValueNullable = null
        verify(prefsEditor).remove(INT_NULLABLE)
    }


    @Test
    fun longReadDefault() {
        whenever(prefs.contains(LONG)).thenReturn(false)
        assertEquals(1L, prefsContainer.longValue)
    }

    @Test
    fun longRead() {
        whenever(prefs.contains(LONG)).thenReturn(true)
        whenever(prefs.getLong(LONG, 0)).thenReturn(2L)
        assertEquals(2L, prefsContainer.longValue)
    }

    @Test
    fun longSave() {
        prefsContainer.longValue = 3L
        verify(prefsEditor).putLong(LONG, 3L)
    }

    @Test
    fun longNullableReadDefault() {
        whenever(prefs.contains(LONG_NULLABLE)).thenReturn(false)
        assertNull(prefsContainer.longValueNullable)
    }

    @Test
    fun longNullableRead() {
        whenever(prefs.contains(LONG_NULLABLE)).thenReturn(true)
        whenever(prefs.getLong(LONG_NULLABLE, 0)).thenReturn(2L)
        assertEquals(2L, prefsContainer.longValueNullable)
    }

    @Test
    fun longNullableSave() {
        prefsContainer.longValueNullable = 3L
        verify(prefsEditor).putLong(LONG_NULLABLE, 3L)
    }

    @Test
    fun nullableLongSaveNull() {
        prefsContainer.longValueNullable = null
        verify(prefsEditor).remove(LONG_NULLABLE)
    }


    @Test
    fun floatReadDefault() {
        whenever(prefs.contains(FLOAT)).thenReturn(false)
        assertEquals(1f, prefsContainer.floatValue)
    }

    @Test
    fun floatRead() {
        whenever(prefs.contains(FLOAT)).thenReturn(true)
        whenever(prefs.getFloat(FLOAT, 0f)).thenReturn(2f)
        assertEquals(2f, prefsContainer.floatValue)
    }

    @Test
    fun floatSave() {
        prefsContainer.floatValue = 3f
        verify(prefsEditor).putFloat(FLOAT, 3f)
    }

    @Test
    fun floatNullableReadDefault() {
        whenever(prefs.contains(FLOAT_NULLABLE)).thenReturn(false)
        assertNull(prefsContainer.floatValueNullable)
    }

    @Test
    fun floatNullableRead() {
        whenever(prefs.contains(FLOAT_NULLABLE)).thenReturn(true)
        whenever(prefs.getFloat(FLOAT_NULLABLE, 0f)).thenReturn(2f)
        assertEquals(2f, prefsContainer.floatValueNullable)
    }

    @Test
    fun nullableFloatSave() {
        prefsContainer.floatValueNullable = 3f
        verify(prefsEditor).putFloat(FLOAT_NULLABLE, 3f)
    }

    @Test
    fun nullableFloatSaveNull() {
        prefsContainer.floatValueNullable = null
        verify(prefsEditor).remove(FLOAT_NULLABLE)
    }


    @Test
    fun stringReadDefault() {
        whenever(prefs.contains(STRING)).thenReturn(false)
        assertEquals("a", prefsContainer.stringValue)
    }

    @Test
    fun stringRead() {
        whenever(prefs.contains(STRING)).thenReturn(true)
        whenever(prefs.getString(STRING, null)).thenReturn("b")
        assertEquals("b", prefsContainer.stringValue)
    }

    @Test
    fun stringSave() {
        prefsContainer.stringValue = "c"
        verify(prefsEditor).putString(STRING, "c")
    }

    @Test
    fun stringNullableReadDefault() {
        whenever(prefs.contains(STRING_NULLABLE)).thenReturn(false)
        assertNull(prefsContainer.stringValueNullable)
    }

    @Test
    fun stringNullableRead() {
        whenever(prefs.contains(STRING_NULLABLE)).thenReturn(true)
        whenever(prefs.getString(STRING_NULLABLE, null)).thenReturn("b")
        assertEquals("b", prefsContainer.stringValueNullable)
    }

    @Test
    fun stringNullableSave() {
        prefsContainer.stringValueNullable = "c"
        verify(prefsEditor).putString(STRING_NULLABLE, "c")
    }

    @Test
    fun stringNullableSaveNull() {
        prefsContainer.stringValueNullable = null
        verify(prefsEditor).remove(STRING_NULLABLE)
    }


    @Test
    fun stringSetReadDefault() {
        whenever(prefs.contains(STRING_SET)).thenReturn(false)
        assertEquals(setOf("a"), prefsContainer.stringSetValue)
    }

    @Test
    fun stringSetRead() {
        whenever(prefs.contains(STRING_SET)).thenReturn(true)
        whenever(prefs.getStringSet(STRING_SET, emptySet())).thenReturn(setOf("a", "b"))
        assertEquals(setOf("a", "b"), prefsContainer.stringSetValue)
    }

    @Test
    fun stringSetSave() {
        prefsContainer.stringSetValue = setOf("a", "b", "c")
        verify(prefsEditor).putStringSet(STRING_SET, setOf("a", "b", "c"))
    }

    @Test
    fun stringSetNullableReadDefault() {
        whenever(prefs.contains(STRING_SET_NULLABLE)).thenReturn(false)
        assertNull(prefsContainer.stringSetValueNullable)
    }

    @Test
    fun stringSetNullableRead() {
        whenever(prefs.contains(STRING_SET_NULLABLE)).thenReturn(true)
        whenever(prefs.getStringSet(STRING_SET_NULLABLE, emptySet())).thenReturn(setOf("a", "b"))
        assertEquals(setOf("a", "b"), prefsContainer.stringSetValueNullable)
    }

    @Test
    fun stringSetNullableSave() {
        prefsContainer.stringSetValueNullable = setOf("a", "b", "c")
        verify(prefsEditor).putStringSet(STRING_SET_NULLABLE, setOf("a", "b", "c"))
    }

    @Test
    fun stringSetNullableSaveNull() {
        prefsContainer.stringSetValueNullable = null
        verify(prefsEditor).remove(STRING_SET_NULLABLE)
    }


    @Test
    fun enumReadDefault() {
        whenever(prefs.contains(ENUM)).thenReturn(false)
        assertEquals(SomeEnum.DEFAULT, prefsContainer.enumValue)
    }

    @Test
    fun enumRead() {
        whenever(prefs.contains(ENUM)).thenReturn(true)
        whenever(prefs.getString(ENUM, null)).thenReturn(SomeEnum.NICE.name)
        assertEquals(SomeEnum.NICE, prefsContainer.enumValue)
    }

    @Test
    fun enumReadFallback() {
        whenever(prefs.contains(ENUM)).thenReturn(true)
        whenever(prefs.getString(ENUM, null)).thenReturn("NOT_EXISTED")
        assertEquals(SomeEnum.DEFAULT, prefsContainer.enumValue)
    }

    @Test
    fun enumSave() {
        prefsContainer.enumValue = SomeEnum.CRAP
        verify(prefsEditor).putString(ENUM, SomeEnum.CRAP.name)
    }

    @Test
    fun enumNullableReadDefault() {
        whenever(prefs.contains(ENUM_NULLABLE)).thenReturn(false)
        assertNull(prefsContainer.enumValueNullable)
    }

    @Test
    fun enumNullableRead() {
        whenever(prefs.contains(ENUM_NULLABLE)).thenReturn(true)
        whenever(prefs.getString(ENUM_NULLABLE, null)).thenReturn(SomeEnum.NICE.name)
        assertEquals(SomeEnum.NICE, prefsContainer.enumValueNullable)
    }

    @Test
    fun enumNullableReadFallback() {
        whenever(prefs.contains(ENUM_NULLABLE)).thenReturn(true)
        whenever(prefs.getString(ENUM_NULLABLE, null)).thenReturn("NOT_EXISTED")
        assertNull(prefsContainer.enumValueNullable)
    }

    @Test
    fun enumNullableWrite() {
        prefsContainer.enumValueNullable = SomeEnum.CRAP
        verify(prefsEditor).putString(ENUM_NULLABLE, SomeEnum.CRAP.name)
    }

    @Test
    fun enumNullableWriteNull() {
        prefsContainer.enumValueNullable = null
        verify(prefsEditor).remove(ENUM_NULLABLE)
    }


    @Test
    fun jsonReadDefault() {
        val default = Person("a", 1)
        whenever(prefs.contains(JSON)).thenReturn(false)
        assertEquals(default, prefsContainer.jsonValue)
    }

    @Test
    fun jsonRead() {
        val person = Person("b", 2)
        val personJson = jsonAdapter.toJson(person)
        whenever(prefs.contains(JSON)).thenReturn(true)
        whenever(prefs.getString(JSON, null)).thenReturn(personJson)
        assertEquals(person, prefsContainer.jsonValue)
    }

    @Test
    fun jsonReadFallback() {
        val default = Person("a", 1)
        val personOldFormat = PersonOldFormat("b")
        val personOldFormatJson = jsonOldFormatAdapter.toJson(personOldFormat)
        whenever(prefs.contains(JSON)).thenReturn(true)
        whenever(prefs.getString(JSON, null)).thenReturn(personOldFormatJson)
        assertEquals(default, prefsContainer.jsonValue)
    }

    @Test
    fun jsonSave() {
        val person = Person("c", 3)
        val personJson = jsonAdapter.toJson(person)
        prefsContainer.jsonValue = person
        verify(prefsEditor).putString(JSON, personJson)
    }

    @Test
    fun jsonNullableReadDefault() {
        whenever(prefs.contains(JSON_NULLABLE)).thenReturn(false)
        assertNull(prefsContainer.jsonValueNullable)
    }

    @Test
    fun jsonNullableRead() {
        val person = Person("b", 2)
        val personJson = jsonAdapter.toJson(person)
        whenever(prefs.contains(JSON_NULLABLE)).thenReturn(true)
        whenever(prefs.getString(JSON_NULLABLE, null)).thenReturn(personJson)
        assertEquals(person, prefsContainer.jsonValueNullable)
    }

    @Test
    fun jsonNullableReadFallback() {
        val personOldFormat = PersonOldFormat("b")
        val personOldFormatJson = jsonOldFormatAdapter.toJson(personOldFormat)
        whenever(prefs.contains(JSON_NULLABLE)).thenReturn(true)
        whenever(prefs.getString(JSON_NULLABLE, null)).thenReturn(personOldFormatJson)
        assertNull(prefsContainer.jsonValueNullable)
    }

    @Test
    fun jsonNullableSave() {
        val person = Person("c", 3)
        val personJson = jsonAdapter.toJson(person)
        prefsContainer.jsonValueNullable = person
        verify(prefsEditor).putString(JSON_NULLABLE, personJson)
    }

    @Test
    fun jsonNullableWriteNull() {
        prefsContainer.jsonValueNullable = null
        verify(prefsEditor).remove(JSON_NULLABLE)
    }
}

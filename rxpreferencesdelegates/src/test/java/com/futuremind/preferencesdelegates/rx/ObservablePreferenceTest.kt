package com.futuremind.preferencesdelegates.rx

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ObservablePreferenceTest {

    companion object {
        private const val BOOLEAN = "BOOLEAN"
        private const val INT = "INT"
        private const val LONG = "LONG"
        private const val FLOAT = "FLOAT"
        private const val STRING = "STRING"
        private const val STRING_SET = "STRING_SET"
        private const val ENUM = "ENUM"
        private const val JSON = "JSON"

        private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        private val jsonAdapter = moshi.adapter(Person::class.java)
    }

    enum class SomeEnum { DEFAULT, NICE, CRAP }

    data class Person(val name: String, val age: Int)

    class PrefsValuesContainer(prefs: SharedPreferences) {
        val booleanPref = prefs.observableBoolean(BOOLEAN)
        val intPref = prefs.observableInt(INT)
        val longPref = prefs.observableLong(LONG)
        val floatPref = prefs.observableFloat(FLOAT)
        val stringPref = prefs.observableString(STRING)
        val stringSetPref = prefs.observableStringSet(STRING_SET)
        var enumPref = prefs.observableEnum<SomeEnum>(ENUM, SomeEnum.DEFAULT)
        var jsonPref = prefs.observableJson<Person>(JSON, null, moshi)
    }

    private val intMap = mutableMapOf<String, Int>()

    abstract inner class FakeSharedPreferences: SharedPreferences {
        override fun getInt(key: String, defValue: Int) = intMap[key] ?: defValue
        override fun edit() = prefsEditor
    }

    abstract inner class FakeSharedPreferencesEditor: SharedPreferences.Editor {
        override fun putInt(key: String, value: Int): SharedPreferences.Editor {
            intMap[key] = value
            return this
        }
    }

    @Spy private lateinit var prefs: FakeSharedPreferences
    @Spy private lateinit var prefsEditor: FakeSharedPreferencesEditor
    private lateinit var prefsContainer: PrefsValuesContainer

    @Before
    fun setUp() {
        whenever(prefsEditor.putBoolean(any(), any())).thenReturn(prefsEditor)
        whenever(prefsEditor.putLong(any(), any())).thenReturn(prefsEditor)
        whenever(prefsEditor.putFloat(any(), any())).thenReturn(prefsEditor)
        whenever(prefsEditor.putString(any(), any())).thenReturn(prefsEditor)
        whenever(prefsEditor.putStringSet(any(), any())).thenReturn(prefsEditor)
        prefsContainer = PrefsValuesContainer(prefs)
    }

    @Test
    fun `when save Boolean then SharedPreferences is updated`() {
        prefsContainer.booleanPref.save(true)
        verify(prefsEditor).putBoolean(BOOLEAN, true)
    }

    @Test
    fun `when save Int then SharedPreferences is updated`() {
        prefsContainer.intPref.save(21)
        verify(prefsEditor).putInt(INT, 21)
    }

    @Test
    fun `when save Long then SharedPreferences is updated`() {
        prefsContainer.longPref.save(21L)
        verify(prefsEditor).putLong(LONG, 21L)
    }

    @Test
    fun `when save Float then SharedPreferences is updated`() {
        prefsContainer.floatPref.save(21f)
        verify(prefsEditor).putFloat(FLOAT, 21f)
    }

    @Test
    fun `when save String then SharedPreferences is updated`() {
        prefsContainer.stringPref.save("hey")
        verify(prefsEditor).putString(STRING, "hey")
    }

    @Test
    fun `when save String Set then SharedPreferences is updated`() {
        prefsContainer.stringSetPref.save(setOf("hey", "ho"))
        verify(prefsEditor).putStringSet(STRING_SET, setOf("hey", "ho"))
    }

    @Test
    fun `when save Enum then SharedPreferences is updated`() {
        prefsContainer.enumPref.save(SomeEnum.NICE)
        verify(prefsEditor).putString(ENUM, SomeEnum.NICE.name)
    }

    @Test
    fun `when save json then SharedPreferences is updated`() {
        val person = Person("Johny", 22)
        val personJson = jsonAdapter.toJson(person)
        prefsContainer.jsonPref.save(person)
        verify(prefsEditor).putString(JSON, personJson)
    }

    @Test
    fun `when subscribe before saving item then subscriber gets default and saved item`() {
        val testSubscriber = prefsContainer.intPref.observable().test()
        prefsContainer.intPref.save(21)
        testSubscriber.assertValues(0, 21)
    }

    @Test
    fun `when subscribe after saving item then subscriber gets only saved item`() {
        prefsContainer.intPref.save(21)
        val testSubscriber = prefsContainer.intPref.observable().test()
        testSubscriber.assertValues(21)
    }

    @Test
    fun `when subscribe before saving item twice then subscriber gets saved values including default`() {
        val testSubscriber = prefsContainer.intPref.observable().test()
        prefsContainer.intPref.save(21)
        prefsContainer.intPref.save(58)
        testSubscriber.assertValues(0, 21, 58)
    }

    @Test
    fun `when subscribe in between saving item twice then subscriber gets saved values`() {
        prefsContainer.intPref.save(21)
        val testSubscriber = prefsContainer.intPref.observable().test()
        prefsContainer.intPref.save(58)
        testSubscriber.assertValues(21, 58)
    }

    @Test
    fun `when subscribe after saving item twice then subscriber gets only latest value`() {
        prefsContainer.intPref.save(21)
        prefsContainer.intPref.save(58)
        val testSubscriber = prefsContainer.intPref.observable().test()
        testSubscriber.assertValues(58)
    }

    @Test
    fun `when two subscribes subscribe in between saving item twice then both subscribers get saved values`() {
        prefsContainer.intPref.save(21)
        val testSubscriber = prefsContainer.intPref.observable().test()
        val testSubscriber2 = prefsContainer.intPref.observable().test()
        prefsContainer.intPref.save(58)

        testSubscriber.assertValues(21, 58)
        testSubscriber2.assertValues(21, 58)
    }

    @Test
    fun `when second subscriber subscribes at the end then it gets the last saved value`() {
        prefsContainer.intPref.save(21)
        val testSubscriber = prefsContainer.intPref.observable().test()
        prefsContainer.intPref.save(58)
        val testSubscriber2 = prefsContainer.intPref.observable().test()

        testSubscriber.assertValues(21, 58)
        testSubscriber2.assertValues(58)
    }

    @Test
    fun `when subscribe dispose and subscribe again then subscriber still gets last and subsequent values`() {
        prefsContainer.intPref.save(21)
        val testSubscriber1 = prefsContainer.intPref.observable().test()
        testSubscriber1.dispose()

        val testSubscriber2 = prefsContainer.intPref.observable().test()
        prefsContainer.intPref.save(58)

        testSubscriber2.assertValues(21, 58)
    }
}

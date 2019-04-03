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
    fun `when save item than SharedPreferences is updated`() {
        prefsContainer.intValue = 21
        verify(prefsEditor).putInt(INT, 21)
    }

    @Test
    fun `when subscribe before saving item then subscriber gets default and saved item`() {
        val testSubscriber = prefsContainer.intObservable.test()
        prefsContainer.intValue = 21
        testSubscriber.assertValues(0, 21)
    }

    @Test
    fun `when subscribe after saving item then subscriber gets only saved item`() {
        prefsContainer.intValue = 21
        val testSubscriber = prefsContainer.intObservable.test()
        testSubscriber.assertValues(21)
    }

    @Test
    fun `when subscribe before saving item twice then subscriber gets saved values including default`() {
        val testSubscriber = prefsContainer.intObservable.test()
        prefsContainer.intValue = 21
        prefsContainer.intValue = 58
        testSubscriber.assertValues(0, 21, 58)
    }

    @Test
    fun `when subscribe in between saving item twice then subscriber gets saved values`() {
        prefsContainer.intValue = 21
        val testSubscriber = prefsContainer.intObservable.test()
        prefsContainer.intValue = 58
        testSubscriber.assertValues(21, 58)
    }

    @Test
    fun `when subscribe after saving item twice then subscriber gets only latest value`() {
        prefsContainer.intValue = 21
        prefsContainer.intValue = 58
        val testSubscriber = prefsContainer.intObservable.test()
        testSubscriber.assertValues(58)
    }
}
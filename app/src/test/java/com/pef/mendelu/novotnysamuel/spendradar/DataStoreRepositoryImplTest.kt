package com.pef.mendelu.novotnysamuel.spendradar

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import com.pef.mendelu.novotnysamuel.spendradar.datastore.DataStoreRepositoryImpl
import com.pef.mendelu.novotnysamuel.spendradar.datastore.dataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DataStoreRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var repository: DataStoreRepositoryImpl

    @Before
    fun setup() = runTest {
        context = ApplicationProvider.getApplicationContext()
        repository = DataStoreRepositoryImpl(context)
        context.dataStore.edit { it.clear() }
    }

    @Test
    fun getLanguage_emitsDefaultAndUpdatedValue() = runTest {
        val defaultLang = Locale.getDefault().language
        val initial = repository.getLanguage().first()
        assertEquals(defaultLang, initial)

        repository.setLanguage("cs")

        val updated = repository.getLanguage().first()
        assertEquals("cs", updated)
    }
}

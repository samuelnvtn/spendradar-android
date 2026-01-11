package com.pef.mendelu.novotnysamuel.spendradar

import com.pef.mendelu.novotnysamuel.spendradar.datastore.IDataStoreRepository
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.settings.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

//aktualizuje skutecne lang?

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun setLanguage_updatesStateFlow() = runTest {
        val repository = FakeDataStoreRepository(initial = "en")
        val viewModel = SettingsViewModel(repository)
        val emissions = mutableListOf<String?>()

        val job = launch {
            viewModel.language.collect { value ->
                emissions.add(value)
            }
        }
        viewModel.setLanguage("cs")
        advanceUntilIdle()

        assertEquals("cs", emissions.last())
        job.cancel()
    }

    private class FakeDataStoreRepository(initial: String) : IDataStoreRepository {
        private val languageFlow = MutableStateFlow(initial)

        override suspend fun setFirstRun() {
            throw UnsupportedOperationException("Not needed for this test")
        }

        override suspend fun getFirstRun(): Boolean {
            throw UnsupportedOperationException("Not needed for this test")
        }

        override fun getLanguage(): Flow<String> = languageFlow

        override suspend fun setLanguage(language: String) {
            languageFlow.value = language
        }
    }
}

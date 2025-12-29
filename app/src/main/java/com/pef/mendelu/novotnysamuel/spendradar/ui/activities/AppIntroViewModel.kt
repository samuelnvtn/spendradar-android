package com.pef.mendelu.novotnysamuel.spendradar.ui.activities

import androidx.lifecycle.ViewModel
import com.pef.mendelu.novotnysamuel.spendradar.datastore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppIntroViewModel @Inject constructor(private val dataStoreRepository: IDataStoreRepository) : ViewModel() {

    suspend fun setFirstRun(){
        dataStoreRepository.setFirstRun()
    }
}
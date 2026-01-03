package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.settings

import android.graphics.fonts.SystemFonts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pef.mendelu.novotnysamuel.spendradar.datastore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: IDataStoreRepository) : ViewModel(){

    val language: StateFlow<String?> = repository.getLanguage()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "en")


    fun setLanguage(language: String) {
        viewModelScope.launch {
            repository.setLanguage(language)
        }
    }
}
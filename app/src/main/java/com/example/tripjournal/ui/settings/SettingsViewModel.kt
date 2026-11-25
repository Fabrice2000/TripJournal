package com.example.tripjournal.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tripjournal.data.settings.SettingsManager
import com.example.tripjournal.domain.ImportResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsManager: SettingsManager
) : ViewModel() {

    val themeMode: StateFlow<Int> = settingsManager.themeMode
    val defaultSort: StateFlow<String> = settingsManager.defaultSort
    val confirmDelete: StateFlow<Boolean> = settingsManager.confirmDelete
    val distanceUnit: StateFlow<String> = settingsManager.distanceUnit

    private val _resetResult = MutableSharedFlow<ImportResult?>()
    val resetResult: SharedFlow<ImportResult?> = _resetResult

    fun setThemeMode(mode: Int) {
        settingsManager.setThemeMode(mode)
    }

    fun setDefaultSort(sortOrder: String) {
        settingsManager.setDefaultSort(sortOrder)
    }

    fun setConfirmDelete(enabled: Boolean) {
        settingsManager.setConfirmDelete(enabled)
    }

    fun setDistanceUnit(unit: String) {
        settingsManager.setDistanceUnit(unit)
    }

    fun resetData() {
        viewModelScope.launch {
            val result = settingsManager.resetAndReimportData()
            _resetResult.emit(result)
        }
    }

    class Factory(private val settingsManager: SettingsManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                return SettingsViewModel(settingsManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

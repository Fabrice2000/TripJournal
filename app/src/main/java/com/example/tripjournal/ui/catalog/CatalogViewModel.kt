package com.example.tripjournal.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tripjournal.data.database.entity.Trip
import com.example.tripjournal.data.repository.TripRepository
import com.example.tripjournal.data.settings.SettingsManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModel(
    private val repository: TripRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(settingsManager.getDefaultSort())
    val sortOrder: StateFlow<String> = _sortOrder.asStateFlow()

    val pagedTrips: Flow<PagingData<Trip>> = combine(_searchQuery, _sortOrder) { query, sort ->
        Pair(query, sort)
    }.flatMapLatest { (query, sort) ->
        repository.getTripsPaged(query, sort)
    }.cachedIn(viewModelScope)

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortOrder(sort: String) {
        _sortOrder.value = sort
    }

    class Factory(
        private val repository: TripRepository,
        private val settingsManager: SettingsManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatalogViewModel::class.java)) {
                return CatalogViewModel(repository, settingsManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

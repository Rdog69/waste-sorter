package com.random.example.wastesorterapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.random.example.wastesorterapp.data.repository.WasteItemRepository
import com.random.example.wastesorterapp.model.WasteClassification.WasteType
import com.random.example.wastesorterapp.model.WasteItem
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: WasteItemRepository) : ViewModel() {

    // Current search query
    private val _searchQuery = MutableLiveData<String>("")

    // Current filter type (null means show all)
    private val _filterType = MutableLiveData<WasteType?>(null)

    // Combined search results based on query and filter
    val searchResults: LiveData<List<WasteItem>> = _searchQuery.switchMap { query ->
        if (query.isEmpty()) {
            // If no query, return all or filtered by type
            _filterType.switchMap { type ->
                if (type == null) {
                    repository.getAllWasteItems()
                } else {
                    repository.getWasteItemsByType(type)
                }
            }
        } else {
            // If query exists, search with query
            repository.searchWasteItems(query)
        }
    }

    // Selected waste item (for detail view)
    private val _selectedWasteItem = MutableLiveData<WasteItem?>()
    val selectedWasteItem: LiveData<WasteItem?> = _selectedWasteItem

    // Update search query
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Update filter type
    fun setFilterType(wasteType: WasteType?) {
        _filterType.value = wasteType
    }

    // Select a waste item for detail view
    fun selectWasteItem(wasteItem: WasteItem?) {
        _selectedWasteItem.value = wasteItem
    }

    // Add a new waste item
    fun addWasteItem(wasteItem: WasteItem) {
        viewModelScope.launch {
            repository.insertWasteItem(wasteItem)
        }
    }

    // Clear search query and filters
    fun clearSearch() {
        _searchQuery.value = ""
        _filterType.value = null
    }
}

class SearchViewModelFactory(private val repository: WasteItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
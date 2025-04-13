package com.random.example.wastesorterapp.data.repository

import androidx.lifecycle.LiveData
import com.random.example.wastesorterapp.data.dao.WasteItemDao
import com.random.example.wastesorterapp.model.WasteItem
import com.random.example.wastesorterapp.model.WasteClassification.WasteType

class WasteItemRepository(private val wasteItemDao: WasteItemDao) {

    // Get all waste items
    fun getAllWasteItems(): LiveData<List<WasteItem>> {
        return wasteItemDao.getAllWasteItems()
    }

    // Get waste items filtered by type
    fun getWasteItemsByType(wasteType: WasteType): LiveData<List<WasteItem>> {
        return wasteItemDao.getWasteItemsByType(wasteType)
    }

    // Search waste items by query
    fun searchWasteItems(query: String): LiveData<List<WasteItem>> {
        return wasteItemDao.searchWasteItems(query)
    }

    // Get a specific waste item by ID
    fun getWasteItemById(id: Int): LiveData<WasteItem> {
        return wasteItemDao.getWasteItemById(id)
    }

    // Insert a waste item
    suspend fun insertWasteItem(wasteItem: WasteItem): Long {
        return wasteItemDao.insertWasteItem(wasteItem)
    }

    // Insert multiple waste items
    suspend fun insertAllWasteItems(wasteItems: List<WasteItem>) {
        wasteItemDao.insertAll(wasteItems)
    }

    // Update a waste item
    suspend fun updateWasteItem(wasteItem: WasteItem) {
        wasteItemDao.updateWasteItem(wasteItem)
    }

    // Delete a waste item
    suspend fun deleteWasteItem(id: Int) {
        wasteItemDao.deleteWasteItem(id)
    }

    // Get the count of waste items
    suspend fun getCount(): Int {
        return wasteItemDao.getCount()
    }
}
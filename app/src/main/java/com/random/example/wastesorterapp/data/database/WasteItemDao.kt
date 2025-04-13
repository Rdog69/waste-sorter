package com.random.example.wastesorterapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.random.example.wastesorterapp.model.WasteItem
import com.random.example.wastesorterapp.model.WasteClassification.WasteType

@Dao
interface WasteItemDao {
    @Query("SELECT * FROM waste_items ORDER BY name ASC")
    fun getAllWasteItems(): LiveData<List<WasteItem>>

    @Query("SELECT * FROM waste_items WHERE wasteType = :wasteType ORDER BY name ASC")
    fun getWasteItemsByType(wasteType: WasteType): LiveData<List<WasteItem>>

    @Query("SELECT * FROM waste_items WHERE name LIKE '%' || :query || '%' OR keywords LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchWasteItems(query: String): LiveData<List<WasteItem>>

    @Query("SELECT * FROM waste_items WHERE id = :id")
    fun getWasteItemById(id: Int): LiveData<WasteItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWasteItem(wasteItem: WasteItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(wasteItems: List<WasteItem>)

    @Update
    suspend fun updateWasteItem(wasteItem: WasteItem)

    @Query("DELETE FROM waste_items WHERE id = :id")
    suspend fun deleteWasteItem(id: Int)

    @Query("SELECT COUNT(*) FROM waste_items")
    suspend fun getCount(): Int
}
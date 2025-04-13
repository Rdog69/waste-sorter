package com.random.example.wastesorterapp.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.random.example.wastesorterapp.model.WasteClassification.WasteType

@Entity(tableName = "waste_items")
data class WasteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val wasteType: WasteType,
    val description: String = "",
    val instructions: String,
    val keywords: String = "", // Comma-separated keywords for improved search
    val imageResourceName: String = "" // Optional: reference to drawable resource
)
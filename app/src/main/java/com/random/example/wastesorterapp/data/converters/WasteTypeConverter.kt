package com.random.example.wastesorterapp.data.converters

import androidx.room.TypeConverter
import com.random.example.wastesorterapp.model.WasteClassification.WasteType

class WasteTypeConverter {
    @TypeConverter
    fun fromWasteType(wasteType: WasteType): String {
        return wasteType.name
    }

    @TypeConverter
    fun toWasteType(value: String): WasteType {
        return WasteType.valueOf(value)
    }
}
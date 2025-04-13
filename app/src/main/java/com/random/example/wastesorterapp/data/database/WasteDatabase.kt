package com.random.example.wastesorterapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.random.example.wastesorterapp.data.dao.WasteItemDao
import com.random.example.wastesorterapp.model.WasteItem
import com.random.example.wastesorterapp.data.converters.WasteTypeConverter
import com.random.example.wastesorterapp.model.WasteClassification.WasteType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [WasteItem::class], version = 1, exportSchema = false)
@TypeConverters(WasteTypeConverter::class)
abstract class WasteDatabase : RoomDatabase() {

    abstract fun wasteItemDao(): WasteItemDao

    companion object {
        @Volatile
        private var INSTANCE: WasteDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): WasteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WasteDatabase::class.java,
                    "waste_database"
                )
                    .addCallback(WasteDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                instance
            }
        }

        private class WasteDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.wasteItemDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(wasteItemDao: WasteItemDao) {
            // Check if database is already populated
            if (wasteItemDao.getCount() > 0) return

            // Add sample waste items
            val sampleItems = listOf(
                // Recyclable items
                WasteItem(
                    name = "Plastic Bottle",
                    wasteType = WasteType.RECYCLABLE,
                    description = "Plastic bottles made of PET or HDPE",
                    instructions = "Rinse bottles, remove caps, and flatten if possible. Place in blue recycling bin.",
                    keywords = "bottle, water bottle, soda bottle, pet, hdpe, plastic"
                ),
                WasteItem(
                    name = "Newspaper",
                    wasteType = WasteType.RECYCLABLE,
                    description = "Newspapers, magazines, and printed paper",
                    instructions = "Keep dry and clean. Place in blue recycling bin or paper recycling.",
                    keywords = "paper, news, magazine, print, reading"
                ),
                WasteItem(
                    name = "Aluminum Can",
                    wasteType = WasteType.RECYCLABLE,
                    description = "Soda cans, beer cans, and other metal containers",
                    instructions = "Rinse cans and place in blue recycling bin.",
                    keywords = "can, soda, beer, aluminum, metal, tin"
                ),

                // Compost items
                WasteItem(
                    name = "Fruit Scraps",
                    wasteType = WasteType.COMPOST,
                    description = "Fruit peels, cores, and unused portions",
                    instructions = "Place in green compost bin. Remove any stickers.",
                    keywords = "fruit, peel, apple, banana, orange, food waste"
                ),
                WasteItem(
                    name = "Coffee Grounds",
                    wasteType = WasteType.COMPOST,
                    description = "Used coffee grounds and filters",
                    instructions = "Place used coffee grounds and paper filters in green compost bin.",
                    keywords = "coffee, grounds, filter, breakfast, organic"
                ),
                WasteItem(
                    name = "Yard Trimmings",
                    wasteType = WasteType.COMPOST,
                    description = "Grass clippings, leaves, and small branches",
                    instructions = "Place in green compost bin or yard waste collection.",
                    keywords = "grass, leaves, yard, garden, plant, trim"
                ),

                // Landfill items
                WasteItem(
                    name = "Styrofoam",
                    wasteType = WasteType.LANDFILL,
                    description = "Expanded polystyrene foam packaging",
                    instructions = "Cannot be recycled in most areas. Place in black trash bin.",
                    keywords = "foam, packaging, takeout, container, eps, polystyrene"
                ),
                WasteItem(
                    name = "Used Tissues",
                    wasteType = WasteType.LANDFILL,
                    description = "Used facial tissues, napkins, and paper towels",
                    instructions = "Cannot be recycled or composted if soiled. Place in black trash bin.",
                    keywords = "tissue, napkin, paper towel, kleenex, bathroom"
                ),
                WasteItem(
                    name = "Chip Bags",
                    wasteType = WasteType.LANDFILL,
                    description = "Metallized plastic film packaging for chips and snacks",
                    instructions = "Multi-layer packaging cannot be recycled. Place in black trash bin.",
                    keywords = "chips, snack, packaging, foil, plastic, food"
                )
            )

            wasteItemDao.insertAll(sampleItems)
        }
    }
}
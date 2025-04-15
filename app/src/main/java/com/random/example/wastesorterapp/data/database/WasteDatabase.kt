package com.random.example.wastesorterapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.opencsv.CSVReader
import com.random.example.wastesorterapp.data.dao.WasteItemDao
import com.random.example.wastesorterapp.model.WasteItem
import com.random.example.wastesorterapp.data.converters.WasteTypeConverter
import com.random.example.wastesorterapp.model.WasteClassification.WasteType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader

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
                    .addCallback(WasteDatabaseCallback(scope,context))
                    .build()

                INSTANCE = instance
                instance
            }
        }

        private class WasteDatabaseCallback(
            private val scope: CoroutineScope,private val context: Context
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.wasteItemDao(), context = context )
                    }
                }
            }
        }

        suspend fun populateDatabase(wasteItemDao: WasteItemDao, context: Context) {
            if (wasteItemDao.getCount() > 0) return

            val inputStream = context.assets.open("waste_items.csv")
            val reader = CSVReader(InputStreamReader(inputStream))
            val wasteItems = mutableListOf<WasteItem>()

            reader.readNext() // Skip header row

            reader.forEach { row ->
                if (row.size >= 5) {
                    try {
                        val wasteItem = WasteItem(
                            name = row[0].trim(),
                            wasteType = WasteType.valueOf(row[1].trim().uppercase()),
                            description = row[2].trim(),
                            instructions = row[3].trim(),
                            keywords = row[4].trim(),
                            imageResourceName = row.getOrNull(5)?.trim() ?: ""
                        )
                        wasteItems.add(wasteItem)
                    } catch (e: Exception) {
                        // Optional: log or ignore malformed lines
                        e.printStackTrace()
                    }
                }
            }

            reader.close()
            wasteItemDao.insertAll(wasteItems)
        }
    }
}
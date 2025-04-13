package com.random.example.wastesorterapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.random.example.wastesorterapp.data.database.WasteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WasteSorterApplication : Application() {
    // Application scope
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Lazy database instance
    val database by lazy {
        WasteDatabase.getDatabase(this, applicationScope)
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // Initialize any app-wide components here
    }
}
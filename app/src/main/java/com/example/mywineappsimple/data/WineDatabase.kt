package com.example.mywineappsimple.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WineDataClass::class], version = 1, exportSchema = false)
abstract class WineDatabase : RoomDatabase() {
    abstract fun wineDao(): WineDao

    companion object {
        @Volatile
        private var Instance: WineDatabase? = null

        fun getDatabase(context: Context): WineDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, WineDatabase::class.java, "wine_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
package com.example.mywineappsimple.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WineDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(wine: WineDataClass)

    @Update
    suspend fun update(wine: WineDataClass)

    @Delete
    suspend fun delete(wine: WineDataClass)

    @Query("SELECT * from wines WHERE id = :id")
    fun getWine(id: Int): Flow<WineDataClass>

    @Query("SELECT * from wines ORDER BY name ASC")
    fun getAllWines(): Flow<List<WineDataClass>>

    @Query("SELECT * from wines WHERE name = :name and year = :year")
    fun getWineByNameAndYear(name: String, year: Int): WineDataClass?

    @Query("SELECT * from wines WHERE name LIKE :name")
    fun getWineFilteredByName(name: String): Flow<List<WineDataClass>>

}
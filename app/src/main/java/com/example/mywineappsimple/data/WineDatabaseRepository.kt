package com.example.mywineappsimple.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [WineDataClass] from a given data source.
 */
interface WineDatabaseRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllWinesStream(): Flow<List<WineDataClass>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getWineStream(id: Int): Flow<WineDataClass?>

    /**
     * Insert item in the data source
     */
    suspend fun insertWine(wine: WineDataClass)

    /**
     * Delete item from the data source
     */
    suspend fun deleteWine(wine: WineDataClass)

    /**
     * Update item in the data source
     */
    suspend fun updateWine(wine: WineDataClass)

    /**
     * Retrieve an item from the given data source that matches with the [name] and [year].
     */
    suspend fun getWineByNameAndYear(name: String, year: Int): WineDataClass?

    /**
     * Retrieve all items from the given data source that matches with the [name].
     */
    fun getWineFilteredByName(name: String): Flow<List<WineDataClass>>
}
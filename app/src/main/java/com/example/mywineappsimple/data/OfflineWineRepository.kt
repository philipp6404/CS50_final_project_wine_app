package com.example.mywineappsimple.data

import kotlinx.coroutines.flow.Flow

class OfflineWineRepository(private val wineDao: WineDao) : WineDatabaseRepository {
    override fun getAllWinesStream(): Flow<List<WineDataClass>> = wineDao.getAllWines()

    override fun getWineStream(id: Int): Flow<WineDataClass?> = wineDao.getWine(id)

    override suspend fun insertWine(wine: WineDataClass) = wineDao.insert(wine)

    override suspend fun deleteWine(wine: WineDataClass) = wineDao.delete(wine)

    override suspend fun updateWine(wine: WineDataClass) = wineDao.update(wine)

    override suspend fun getWineByNameAndYear(name: String, year: Int): WineDataClass? = wineDao.getWineByNameAndYear(name, year)

    override fun getWineFilteredByName(name: String): Flow<List<WineDataClass>> = wineDao.getWineFilteredByName(name)
}
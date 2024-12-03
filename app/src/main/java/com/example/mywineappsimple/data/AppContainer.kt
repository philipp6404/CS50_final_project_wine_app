package com.example.mywineappsimple.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val wineDatabaseRepository: WineDatabaseRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineWineRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [WineDatabaseRepository]
     */
    override val wineDatabaseRepository: WineDatabaseRepository by lazy {
        OfflineWineRepository(WineDatabase.getDatabase(context).wineDao())
    }
}
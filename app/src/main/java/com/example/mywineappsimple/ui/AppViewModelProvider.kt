package com.example.mywineappsimple.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mywineappsimple.WineApplication
import com.example.mywineappsimple.ui.viewmodel.DetailViewModel
import com.example.mywineappsimple.ui.viewmodel.InputViewModel
import com.example.mywineappsimple.ui.viewmodel.HomeViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for WineViewModel
        initializer {
            InputViewModel(wineApplication().container.wineDatabaseRepository)
        }
        // Initializer for DetailViewModel
        initializer {
            DetailViewModel(
                this.createSavedStateHandle(),
                wineApplication().container.wineDatabaseRepository
            )
        }
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(wineApplication().container.wineDatabaseRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [MyWineApp].
 */
fun CreationExtras.wineApplication(): WineApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as WineApplication)
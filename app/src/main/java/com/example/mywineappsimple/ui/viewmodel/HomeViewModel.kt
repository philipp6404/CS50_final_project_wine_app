package com.example.mywineappsimple.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywineappsimple.data.WineDataClass
import com.example.mywineappsimple.data.WineDatabaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlin.text.first
import kotlin.text.isBlank

class HomeViewModel(
    private val wineDatabaseRepository: WineDatabaseRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    /*
     * StateFlow variable holding the Stream from Database
     */
    val homeUiState: StateFlow<HomeUiState> =
        wineDatabaseRepository.getAllWinesStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    /*
 * variable for holding string of filter text
 */
    var filterTextUiState by mutableStateOf(FilterUiState())

    fun updateFilterTextUiState(input: FilterUiState) {
        filterTextUiState = FilterUiState(filter = input.filter)
    }

    fun clearFilter() {
        filterTextUiState = FilterUiState(filter = "")
    }

    /*
     * StateFlow variable holding the Stream from Database with filter
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val filterUiState: StateFlow<FilterUiState> = snapshotFlow {
        filterTextUiState.filter
    }.mapLatest {
        filterText ->
        val filteredWines =  wineDatabaseRepository.getWineFilteredByName("%$filterText%").first()
        FilterUiState(filter = filterText, filteredList = filteredWines)
    }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
    initialValue = FilterUiState()
    )
}

/*
 * Data Class to list HomeScreen List
 */
data class HomeUiState(
    val wineList: List<WineDataClass> = listOf(),
)

data class FilterUiState(
    var filter: String = "",
    var filteredList: List<WineDataClass> = listOf()
)
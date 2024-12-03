package com.example.mywineappsimple.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywineappsimple.data.WineDataClass
import com.example.mywineappsimple.data.WineDatabaseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import com.example.mywineappsimple.ui.viewmodel.InputViewModel

class DetailViewModel (
    savedstateHandle: SavedStateHandle,
    private val wineDatabaseRepository: WineDatabaseRepository
) : ViewModel() {

    private val wineId: Int = savedstateHandle.get<Int>("id") ?: 0

    val detailsUiState: StateFlow<DetailsUiState> =
        wineDatabaseRepository.getWineStream(wineId)
            .filterNotNull()
            .map { DetailsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    /*
     * this function adds 1 Quantity to wine
     */
    suspend fun addQuantity() {
        val currentQuantity = detailsUiState.value.wineDetail.quantity
        val newQuantity = currentQuantity + 1
        val newEntry: WineDataClass = detailsUiState.value.wineDetail.copy(quantity = newQuantity)
        wineDatabaseRepository.updateWine(newEntry)
    }

    /*
     * this function reduce 1 Quantity to wine
     */
    suspend fun reduceQuantity() {
        val currentQuantity = detailsUiState.value.wineDetail.quantity
        val newQuantity = currentQuantity - 1
        val newEntry: WineDataClass = detailsUiState.value.wineDetail.copy(quantity = newQuantity)
        wineDatabaseRepository.updateWine(newEntry)
    }

    /*
     * this function deletes wine from database
     */
    suspend fun deleteWine() {
        wineDatabaseRepository.deleteWine(detailsUiState.value.wineDetail)
    }

    data class DetailsUiState(
        val wineDetail: WineDataClass = WineDataClass(),
    )
}
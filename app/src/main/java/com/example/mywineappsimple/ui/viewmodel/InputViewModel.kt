package com.example.mywineappsimple.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mywineappsimple.data.WineDataClass
import com.example.mywineappsimple.data.WineDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InputViewModel (
    private val wineDatabaseRepository: WineDatabaseRepository
): ViewModel() {

    /*
     * variable for holding strings of input text
     */
    var inputUiState by mutableStateOf(WineInputText())

    /*
     * variable for holding boolean value of save button
     */
    var isEntryValid by mutableStateOf(false)

    /*
     * function for updating input text
     */
    fun updateInputUiState(input: WineInputText) {
        inputUiState =
            WineInputText(
                name = input.name,
                year = input.year,
                quantity = input.quantity,
                color = input.color,
                taste = input.taste
            )
        isEntryValid = validateInput(inputUiState)
    }

    /*
     * function for validating input text
     */
    private fun validateInput(input: WineInputText): Boolean {
        return with(input) {
            val isNameValid = name.isNotBlank() &&
                    name[0].isUpperCase()
            val isYearValid = year.isNotBlank() &&
                    year.matches(Regex("\\d{4}")) &&
                    year.toIntOrNull() != null
            val isQuantityValid = quantity.isNotBlank() &&
                    quantity.toIntOrNull() != null &&
                    quantity.toInt() > 0
            val isColorValid = color.isNotBlank()
            val isTasteValid = taste.isNotBlank()

            isNameValid && isYearValid && isQuantityValid && isColorValid && isTasteValid
        }
    }

    /*
     * this function convert WineText to WineData class and input in database if valid
     */
    suspend fun saveWine() {
            wineDatabaseRepository.insertWine(inputUiState.toWineDataClass())
    }

    /*
     * this function updates wine in database
     */
    private suspend fun updateWine(newEntry: WineDataClass) {
        wineDatabaseRepository.updateWine(newEntry)
    }

    /*
     * this function check if input is already in database
     */
    suspend fun inputExists(): Boolean {
        return withContext(Dispatchers.IO) { // Use withContext for database query
            val existingWine = getWineByNameAndYear(inputUiState.name, inputUiState.year.toInt())
            existingWine != null
        }
    }

    /*
     * this function gets wine with given name and year from database
     */
    private suspend fun getWineByNameAndYear(name: String, year: Int): WineDataClass? {
        return withContext(Dispatchers.IO) {
            wineDatabaseRepository.getWineByNameAndYear(name, year)
        }
    }

    /*
     * this function updates wine quantity in database
     */
    suspend fun updateQuantity() {
        withContext(Dispatchers.IO) {
            val existingWine = getWineByNameAndYear(inputUiState.name, inputUiState.year.toInt())
            val currentQuantity = existingWine?.quantity ?: 0
            val newQuantity = currentQuantity + inputUiState.quantity.toInt()
            val newEntry: WineDataClass = existingWine!!.copy(quantity = newQuantity)
            updateWine(newEntry)
        }
    }
}

/*
 * Data Class of Text Information for InputForm
 */
data class WineInputText (
    val id: Int = 0,
    val name: String = "",
    val year: String = "",
    val quantity: String = "",
    val color: String = "",
    val taste: String = ""
)

/*
 * convert WineTextInfo to WineDataClass
 */
fun WineInputText.toWineDataClass(): WineDataClass = WineDataClass(
    id = id,
    name = name,
    year = year.toIntOrNull() ?: 0,
    quantity = quantity.toIntOrNull() ?: 0,
    color = color,
    taste = taste
)
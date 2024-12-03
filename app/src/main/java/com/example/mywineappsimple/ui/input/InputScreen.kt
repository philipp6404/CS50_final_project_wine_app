package com.example.mywineappsimple.ui.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mywineappsimple.R
import com.example.mywineappsimple.ui.AppViewModelProvider
import com.example.mywineappsimple.ui.theme.MyWineAppSimpleTheme
import com.example.mywineappsimple.ui.viewmodel.InputViewModel
import com.example.mywineappsimple.ui.viewmodel.WineInputText
import kotlinx.coroutines.launch

@Composable
fun InputScreen(
    navController: NavController,
    viewModel: InputViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    InputBody(
        navController = navController,
        inputUiState = viewModel.inputUiState,
        onValueChange = viewModel::updateInputUiState,
        isEntryValid = viewModel.isEntryValid,
        onSaveClick = {
            coroutineScope.launch {
                if (viewModel.inputExists()) {
                    viewModel.updateQuantity()
                    navController.navigateUp()
                } else {
                    viewModel.saveWine()
                    navController.navigateUp()
                }
            }
        }
    )
}

@Composable
fun InputBody(
    modifier: Modifier = Modifier,
    navController: NavController,
    inputUiState: WineInputText,
    onValueChange: (WineInputText) -> Unit = {},
    onSaveClick:() -> Unit,
    isEntryValid: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight(0.4F)
                .padding(all = dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(R.string.input_screen_header_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
            InputForm(
                inputUiState = inputUiState,
                onValueChange = onValueChange,
            )
        }
        Column(
            modifier = modifier.
                fillMaxHeight(0.2F)
        ) {
            RadioSelection(
                inputUiState = inputUiState,
                onValueChange = onValueChange)
        }
        Column(
            modifier = modifier
                .fillMaxHeight(0.3F)
                .padding(all = dimensionResource(id = R.dimen.padding_small)),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                /*
                 * Cancel Button, go back to Home Screen
                 */
                OutlinedButton(
                    onClick = { navController.navigateUp() },
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
                /*
                 * Save Button, save Input in database, only enable if Input is valid
                 */
                Button(
                    onClick = onSaveClick,
                    enabled = isEntryValid
                ) {
                    Text(stringResource(R.string.save_button))
                }
            }
        }
    }
}

/*
 * Composable for InputForm of Wine
 */
@Composable
fun InputForm(
    modifier: Modifier = Modifier,
    inputUiState: WineInputText,
    onValueChange: (WineInputText) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            label = { Text(stringResource(R.string.input_label_name)) },
            value = inputUiState.name,
            onValueChange = { onValueChange(inputUiState.copy(name = it)) },
        )
        OutlinedTextField(
            label = { Text(stringResource(R.string.input_label_year)) },
            value = inputUiState.year,
            onValueChange = { onValueChange(inputUiState.copy(year = it)) },
        )
        OutlinedTextField(
            label = { Text(stringResource(R.string.input_label_quantity)) },
            value = inputUiState.quantity,
            onValueChange = { onValueChange(inputUiState.copy(quantity = it)) },
        )
    }
}

@Composable
fun RadioSelection(
    inputUiState: WineInputText,
    onValueChange: (WineInputText) -> Unit = {}
) {
    val colors = listOf("red", "white", "rosé")
    val tastes = listOf("dry", "semi-dry", "sweet")
    var selectedColor by remember { mutableStateOf("") }
    var selectedTaste by remember { mutableStateOf("") }

    onValueChange(inputUiState.copy(color = selectedColor, taste = selectedTaste))

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        /*
         * Row for colors
         */
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            colors.forEach { color ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (color == selectedColor),
                        onClick = {
                            selectedColor = color
                            onValueChange(inputUiState.copy(color = selectedColor))
                        }
                    )
                    Text(text = color)
                }
            }
        }
        /*
         * Row for tastes
         */
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tastes.forEach { taste ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (taste == selectedTaste),
                        onClick = {
                            selectedTaste = taste
                            onValueChange(inputUiState.copy(taste = selectedTaste))
                        }
                    )
                    Text(text = taste)
                }
            }
        }
    }

}

/*
 * Preview Section starts here __________________________________________________________/
 */

/*
 * Preview for RadioButton Selection
 */
@Preview(showBackground = true)
@Composable
private fun ColorSelectionPreview() {
    MyWineAppSimpleTheme {
        RadioSelection(inputUiState = WineInputText())
    }
}

/*
 * Preview for InputForm of Wine
 */
@Preview(showBackground = true)
@Composable
private fun WineInputFormPreview() {
    MyWineAppSimpleTheme {
        InputForm(inputUiState = WineInputText())
    }
}

/*
 * Preview for InputBody
 */
@Preview(showBackground = true)
@Composable
private fun WineInputBodyPreview(
) {
    MyWineAppSimpleTheme {

        val inputText = WineInputText(name = "Müller-Thurgau", year = "2010", quantity = "5")

        InputBody(
            navController = NavController(LocalContext.current),
            inputUiState = inputText,
            onValueChange = {},
            isEntryValid = true,
            onSaveClick = {}
        )
    }
}
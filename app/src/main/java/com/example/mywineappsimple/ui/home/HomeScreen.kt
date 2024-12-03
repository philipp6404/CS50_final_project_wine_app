package com.example.mywineappsimple.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.mywineappsimple.data.WineDataClass
import com.example.mywineappsimple.ui.AppViewModelProvider
import com.example.mywineappsimple.ui.theme.MyWineAppSimpleTheme
import com.example.mywineappsimple.ui.viewmodel.FilterUiState
import com.example.mywineappsimple.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    /*
     * variable for holding the stream of the database
     */
    val homeUiState = viewModel.homeUiState.collectAsState()

    /*
     * variable for holding the stream of the filter
     */
    val filterUiState = viewModel.filterUiState.collectAsState()

    HomeBody(
        navController = navController,
        wineList = homeUiState.value.wineList,
        filterTextUiState = viewModel.filterTextUiState,
        onValueChange = viewModel::updateFilterTextUiState,
        filteredList = filterUiState.value.filteredList,
        clearFilter = viewModel::clearFilter
    )
}

@Composable
fun HomeBody(
    wineList: List<WineDataClass>,
    modifier: Modifier = Modifier,
    navController: NavController,
    filterTextUiState: FilterUiState,
    onValueChange: (FilterUiState) -> Unit = {},
    filteredList: List<WineDataClass>,
    clearFilter: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*
         * if database is empty
         */
        if (wineList.isEmpty()) {
            EmptyDatabase(
                modifier = modifier,
                navController = navController
            )
        } else {
            /*
             * if database is not empty
             */
            Column(
                modifier = modifier
                    .weight(0.25f)
                    .padding(all = dimensionResource(id = R.dimen.padding_medium)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                /*
                 * Filter for List
                 */
                OutlinedTextField(
                    label = { Text(stringResource(R.string.filter_label)) },
                    value = filterTextUiState.filter,
                    onValueChange = { onValueChange(filterTextUiState.copy(filter = it)) },
                )
                /*
                 * Header for list
                 */
                Text(
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
                    text = stringResource(R.string.list_label),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
            /*
             * List Section
             */
            Column(
                modifier = modifier
                    .weight(0.75f)
                    .padding(all = dimensionResource(id = R.dimen.padding_medium)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (filterTextUiState.filter.isEmpty()) {
                    /*
                     * unfiltered list
                     */
                    WineList(
                        wineList = wineList,
                        navController = navController,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_small)),
                        clearFilter = clearFilter
                    )
                }
                else {
                    /*
                     * filtered List
                     */
                    WineList(
                        wineList = filteredList,
                        navController = navController,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_small)),
                        clearFilter = clearFilter
                    )
                }
            }
            /*
             * Button Section
             */
            Column(
                modifier = modifier
                    .padding(all = dimensionResource(id = R.dimen.padding_extra_large))
            ) {
                Button(
                    onClick = {
                        navController.navigate("input")
                        clearFilter()
                              },
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                ) {
                    Text(text = stringResource(R.string.add_new_wine))
                }
            }
        }
    }
}

/*
 * Composable for display Text Empty Database
 */
@Composable
fun EmptyDatabase(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Column(
        modifier = modifier
            .fillMaxHeight(0.5F)
            .padding(all = dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = stringResource(R.string.no_wines),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
    }
    /*
             * Button Section
             */
    Column(
        modifier = modifier
            .padding(all = dimensionResource(id = R.dimen.padding_extra_large))
    ) {
        Button(
            onClick = { navController.navigate("input") },
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(text = stringResource(R.string.add_new_wine))
        }
    }
}

/*
 * Composable for listing wines
 */
@Composable
fun WineList(
    wineList: List<WineDataClass>,
    modifier: Modifier = Modifier,
    navController: NavController,
    clearFilter: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = wineList, key = { it.id }) { item ->
            WineCard(
                item,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navController.navigate("detail/${item.id}")
                        clearFilter()
                    }
            )
        }
    }
}

/*
 * Composable for single Card with information about one wine from database
 */
@Composable
fun WineCard(
    wine: WineDataClass,
    modifier: Modifier = Modifier,
) {
    val paddingValues = dimensionResource(R.dimen.padding_medium)

    ElevatedCard(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth(0.7F)
                    .padding(start = paddingValues)
            ) {
                Text(text = wine.name,
                    modifier = Modifier.padding(top = paddingValues),
                    style = MaterialTheme.typography.headlineMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.year_of_wine),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${wine.year}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.taste_of_wine),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = wine.taste,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.color_of_wine),
                        modifier = Modifier.padding(bottom = paddingValues),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = wine.color,
                        modifier = Modifier.padding(bottom = paddingValues),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(end = paddingValues)
            ) {
                Text(
                    text = stringResource(id = R.string.wine_quantity),
                    modifier = Modifier.padding(top = paddingValues),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${wine.quantity}",
                    modifier = Modifier.padding(bottom = paddingValues),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

/*
 * Preview Section starts here __________________________________________________________/
 */

/*
 * Preview for wine card composable
 */
@Preview(showBackground = true)
@Composable
private fun WineCardPreview() {
    val preViewWine = WineDataClass(
        id = 1,
        name = "Sauvignon",
        year = 2010,
        quantity = 5,
        color = "red",
        taste = "dry"
    )
    MyWineAppSimpleTheme {
        WineCard(wine = preViewWine)
    }
}

/*
 * Preview for body of home screen
 */
@Preview(showBackground = true)
@Composable
private fun HomeBodyPreview() {
    val filterText = FilterUiState(filter = "")
    val wineList = listOf(
        WineDataClass(id = 1, name = "Müller-Thurgau", year = 2010, quantity = 3),
        WineDataClass(id = 2, name = "Rotkäppchen", year = 2020, quantity = 5),
        WineDataClass(id = 3, name = "Sauvignon", year = 2022, quantity = 10)
    )
    MyWineAppSimpleTheme {
        HomeBody(
            navController = NavController(LocalContext.current),
            wineList = wineList,
            filterTextUiState = filterText,
            onValueChange = {},
            filteredList = listOf()
        )
    }
}

/*
 * Preview for Empty database message
 */
@Preview(showBackground = true)
@Composable
private fun HomeBodyEmptyListPreview() {
    val filterText = FilterUiState(filter = "test")
    MyWineAppSimpleTheme {
        HomeBody(
            navController = NavController(LocalContext.current),
            wineList = listOf(),
            filterTextUiState = filterText,
            onValueChange = {},
            filteredList = listOf()
        )
    }
}
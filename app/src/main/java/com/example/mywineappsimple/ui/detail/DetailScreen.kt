package com.example.mywineappsimple.ui.detail

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mywineappsimple.R
import com.example.mywineappsimple.data.WineDataClass
import com.example.mywineappsimple.ui.AppViewModelProvider
import com.example.mywineappsimple.ui.home.WineCard
import com.example.mywineappsimple.ui.theme.MyWineAppSimpleTheme
import com.example.mywineappsimple.ui.viewmodel.DetailViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val wineDetail = viewModel.detailsUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    DetailBody(
        navController = navController,
        wineDetail = wineDetail.value.wineDetail,
        onAddClick = {
            coroutineScope.launch {
                viewModel.addQuantity()
            }
        },
        onReduceClick = {
            coroutineScope.launch {
                viewModel.reduceQuantity()
            }
        },
        onDeleteClick = {
            coroutineScope.launch {
                viewModel.deleteWine()
                navController.navigateUp()
            }
        }
    )
}
@Composable
fun DetailBody(
    modifier: Modifier = Modifier,
    navController: NavController,
    wineDetail: WineDataClass,
    onAddClick: () -> Unit = {},
    onReduceClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight(0.5F)
                .padding(all = dimensionResource(id = R.dimen.padding_extra_large)),
            verticalArrangement = Arrangement.Bottom
        ) {
            WineCard(wine = wineDetail)
        }
        Column(
            modifier = modifier
                .fillMaxHeight(0.5F)
                .padding(all = dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.detail_screen_header_text),
                textAlign = TextAlign.Right,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth(0.9F)
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                /*
                 * Back Button
                 */
                OutlinedButton(
                    onClick = { navController.navigateUp() },
                ) {
                    Text(stringResource(R.string.back_button))
                }
                /*
                 * Reduce Quantity Button
                 */
                Button(onClick = onReduceClick) {
                    Text(text = "-", fontWeight = FontWeight.Bold)
                }
                /*
                 * Add Quantity Button
                 */
                Button(onClick = onAddClick) {
                    Text(text = "+", fontWeight = FontWeight.Bold)
                }
            }
        }
        Column(
            modifier = modifier
                .fillMaxHeight(0.8F)
                .padding(all = dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.delete_text),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
            Button(onClick = onDeleteClick) {
                Text(text = stringResource(R.string.delete_button))
            }
        }
    }
}

/*
 * Preview Section starts here __________________________________________________________/
 */

/*
 * Preview for DetailBody
 */
@Preview(showBackground = true)
@Composable
private fun DetailBodyPreview() {
    val preViewWine = WineDataClass(
        id = 1,
        name = "Müller-Thurgau",
        year = 2010,
        quantity = 5
    )
    MyWineAppSimpleTheme {
        DetailBody(
            navController = NavController(LocalContext.current),
            wineDetail = preViewWine)
}
}
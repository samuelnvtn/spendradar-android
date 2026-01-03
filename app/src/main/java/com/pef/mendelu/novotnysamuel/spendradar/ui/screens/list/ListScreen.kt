package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BaseScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.PlaceholderScreenContent

@Composable
fun ListScreen(){

    BaseScreen(
        bottomBarContent = {
            BottomNavBar(
                //selectedDestination = Destination.BeersListScreen,
                //navigation = navigation
            )
        },
        placeholderScreenContent = if (beers.size == 0) {
            PlaceholderScreenContent(
                image = R.drawable.undraw_beer,
                text = (stringResource(R.string.beer_emptylist))
            )
        } else null,

        actions = {
            IconButton(onClick = {
                //navigation.navigateToSettings()
            }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }},
        topBarText = (stringResource(R.string.beers)),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                //navigation.navigateToAddEditBeer(id = null)
            }, containerColor = lightButton, contentColor = darkTextColor
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        ListScreenContent(
            paddingValues = it,
            //beers = beers,
            //navigation = navigation,

            //actions = viewModel
        )
    }
}

@Composable
fun ListScreenContent(
    paddingValues: PaddingValues,
   // beers: List<Beer>,
    //navigation: INavigationRouter,
){

}
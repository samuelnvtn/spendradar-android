package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pef.mendelu.novotnysamuel.spendradar.navigation.Destination
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BaseScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BottomNavBar
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.PlaceholderScreenContent
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.accentColor

@Composable
fun ListScreen(
    navigation: INavigationRouter
){
    var beers = 0

    BaseScreen(
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.ListScreen,
                navigation = navigation
            )
        },
        placeholderScreenContent = if (beers == 0) {
            PlaceholderScreenContent(
                image = R.drawable.undraw_empty_void,
                text = (stringResource(R.string.emptylist)
            )
            )
        } else null,

        actions = {
            IconButton(onClick = {
                navigation.navigateToSettings()
            }) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            }},
        topBarText = (stringResource(R.string.receipts)),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                //navigation.navigateToAddEditBeer(id = null)
            }, containerColor = barButton, contentColor = accentColor,
                modifier = Modifier.offset(y = (-16).dp)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_barcode_scanner),
                    contentDescription = null
                )
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
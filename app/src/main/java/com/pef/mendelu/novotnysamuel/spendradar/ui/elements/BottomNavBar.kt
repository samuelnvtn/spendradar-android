package com.pef.mendelu.novotnysamuel.spendradar.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.pef.mendelu.novotnysamuel.spendradar.navigation.Destination
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import kotlin.collections.forEach
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor


data class BottomNavItem(
    val label: String,
    val destination: Destination,
    val icon: ImageVector? = null,
    val iconRes: Int? = null
)

@Composable
fun BottomNavBar(
    selectedDestination: Destination,
    navigation: INavigationRouter
) {
    val items = listOf(
        BottomNavItem(
            label = (stringResource(R.string.list)),
            destination = Destination.ListScreen,
            icon = Icons.Outlined.Home
        ),
        BottomNavItem(
            label = (stringResource(R.string.map)),
            destination = Destination.MapScreen,
            icon = Icons.Outlined.LocationOn
        ),
        BottomNavItem(
            label = (stringResource(R.string.stats)),
            destination = Destination.TestScreen,
            iconRes = R.drawable.chart
        ),
    )

    NavigationBar(
        containerColor = barColor,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedDestination == item.destination,
                onClick = {
                    when (item.destination) {
                        Destination.ListScreen -> navigation.navigateToList()
                        Destination.MapScreen -> navigation.navigateToMap()
                      //  Destination.CalendarScreen -> navigation.navigateToCalendar()
                      //  Destination.StatsScreen -> navigation.navigateToStats()
                        else -> {}
                    }

                },
                icon = {
                    if (item.icon != null) {
                        Icon(item.icon, contentDescription = item.label)
                    } else if (item.iconRes != null) {
                        Icon(painter = painterResource(id = item.iconRes), contentDescription = item.label)
                    }
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = textColor,
                    selectedTextColor = textColor,
                    unselectedIconColor = textColor,
                    unselectedTextColor = textColor,
                    indicatorColor = barButton
                )
            )
        }
    }
}
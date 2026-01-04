package com.pef.mendelu.novotnysamuel.spendradar.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun navigateToList() {
        navController.navigate(Destination.ListScreen.route)
    }

    override fun navigateToAdd() {
        navController.navigate(Destination.AddScreen.route)
    }

    override fun navigateToMap() {
        navController.navigate(Destination.MapScreen.route)
    }

    override fun navigateToSettings() {
        navController.navigate(Destination.SettingsScreen.route)
    }

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun navigateTest() {
        navController.navigate(Destination.TestScreen.route)
    }

}
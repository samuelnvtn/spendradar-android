package com.pef.mendelu.novotnysamuel.spendradar.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun navigateToList() {
        navController.navigate(Destination.ListScreen.route)
    }

    override fun navigateToAdd() {
        TODO("Not yet implemented")
    }

    override fun navigateToMap() {
        TODO("Not yet implemented")
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
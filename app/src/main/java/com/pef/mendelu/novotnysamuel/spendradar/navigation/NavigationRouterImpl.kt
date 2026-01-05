package com.pef.mendelu.novotnysamuel.spendradar.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun navigateToList() {
        navController.navigate(Destination.ListScreen.route)
    }

    override fun navigateToAdd(id: Long?) {
        if (id == null) {
            navController.navigate(Destination.AddScreen.route)
        } else {
            navController.navigate("${Destination.AddScreen.route}/${id}")
        }
    }

    override fun navigateToMap() {
        navController.navigate(Destination.MapScreen.route)
    }

    override fun navigateToDetail(id: Long) {
        navController.navigate("${Destination.DetailScreen.route}/${id}")
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

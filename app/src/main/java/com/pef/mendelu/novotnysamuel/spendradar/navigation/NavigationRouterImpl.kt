package com.pef.mendelu.novotnysamuel.spendradar.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun navigateToList() {
        navController.navigate(Destination.ListScreen.route)
    }

    override fun navigateToAdd(id: Long?, fromCamera: Boolean) {
        val fromCameraParam = "fromCamera=$fromCamera"
        if (id == null) {
            navController.navigate("${Destination.AddScreen.route}?$fromCameraParam")
        } else {
            navController.navigate("${Destination.AddScreen.route}/${id}?$fromCameraParam")
        }
    }

    override fun navigateToMap() {
        navController.navigate(Destination.MapScreen.route)
    }

    override fun navigateToStats() {
        navController.navigate(Destination.StatsScreen.route)
    }

    override fun navigateToDetail(id: Long) {
        navController.navigate("${Destination.DetailScreen.route}/${id}")
    }

    override fun navigateToTextRecognition() {
        navController.navigate(Destination.TextRecognitionScreen.route)
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

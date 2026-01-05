package com.pef.mendelu.novotnysamuel.spendradar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.TestScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.add.AddScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.camera.TextRecognitionScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.detail.DetailScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.list.ListScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.map.MapScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.settings.SettingsScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.stats.StatsScreen

@Composable
fun NavGraph(
    startDestination: String,
    navHostController: NavHostController = rememberNavController(),
    navRouter: INavigationRouter = remember {
        NavigationRouterImpl(navHostController)
    }
){

    NavHost(navController = navHostController, startDestination = startDestination) {

        composable(route = Destination.ListScreen.route) {
            ListScreen(navRouter)
        }

        composable(route = Destination.MapScreen.route) {
            MapScreen(navRouter)
        }

        composable(route = Destination.StatsScreen.route) {
            StatsScreen(navRouter)
        }

        composable(
            route = "${Destination.AddScreen.route}?fromCamera={fromCamera}",
            arguments = listOf(
                navArgument("fromCamera") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            val fromCamera = it.arguments?.getBoolean("fromCamera") ?: false
            AddScreen(navRouter, fromCamera = fromCamera)
        }

        composable(route = "${Destination.AddScreen.route}/{id}?fromCamera={fromCamera}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument("fromCamera") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ){
            val id = it.arguments?.getLong("id")
            val fromCamera = it.arguments?.getBoolean("fromCamera") ?: false
            AddScreen(navRouter, id, fromCamera)
        }

        composable(route = "${Destination.DetailScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val id = it.arguments?.getLong("id")
            if (id != null) {
                DetailScreen(navRouter, id)
            }
        }

        composable(route = Destination.TestScreen.route) {
            TestScreen(navRouter)
        }

        composable(route = Destination.SettingsScreen.route) {
            SettingsScreen(navRouter)
        }

        composable(route = Destination.TextRecognitionScreen.route) {
            TextRecognitionScreen(navRouter)
        }
/*
        composable(route = Destination.AddEditBeerScreen.route) {
            AddEditBeerScreen(navRouter, null)
        }

        composable(route = "${Destination.AddEditBeerScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val id = it.arguments?.getLong("id")
            if(id != null) {
                AddEditBeerScreen(navRouter, id)
            }
        }




        composable(route = Destination.CalendarScreen.route) {
            CalendarScreen(navRouter)
        }

        composable(route = Destination.StatsScreen.route) {
            StatsScreen(navRouter)
        }

        composable(route = "${Destination.DetailScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val id = it.arguments?.getLong("id")
            if (id != null) {
                DetailScreen(navRouter, id)
            }
        }*/
    }

}

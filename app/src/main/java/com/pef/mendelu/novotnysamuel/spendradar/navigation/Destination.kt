package com.pef.mendelu.novotnysamuel.spendradar.navigation

sealed class Destination(val route: String) {
    object ListScreen : Destination("list")
    object MapScreen : Destination("map")
    object StatsScreen : Destination("stats")
    object AddScreen : Destination("add")
    object DetailScreen : Destination("detail")
    object SettingsScreen : Destination("settings")

    object TestScreen : Destination("test")

}
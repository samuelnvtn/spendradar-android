package com.pef.mendelu.novotnysamuel.spendradar.navigation

interface INavigationRouter {

    fun navigateToList()
    fun navigateToAdd()
    fun navigateToMap()
    fun navigateToSettings()
    fun returnBack()

    fun navigateTest()
}
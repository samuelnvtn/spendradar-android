package com.pef.mendelu.novotnysamuel.spendradar.navigation

interface INavigationRouter {

    fun navigateToList()
    fun navigateToAdd(id: Long? = null)
    fun navigateToMap()
    fun navigateToStats()
    fun navigateToDetail(id: Long)
    fun navigateToSettings()
    fun returnBack()

    fun navigateTest()
}

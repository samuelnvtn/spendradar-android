package com.pef.mendelu.novotnysamuel.spendradar.navigation

interface INavigationRouter {

    fun navigateToList()
    fun navigateToAdd(id: Long? = null, fromCamera: Boolean = false)
    fun navigateToMap()
    fun navigateToStats()
    fun navigateToDetail(id: Long)
    fun navigateToTextRecognition()
    fun navigateToSettings()
    fun returnBack()
}

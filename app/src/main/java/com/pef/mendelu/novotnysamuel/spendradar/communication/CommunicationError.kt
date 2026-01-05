package com.pef.mendelu.novotnysamuel.spendradar.communication

sealed class CommunicationError {
    data object NoInternet : CommunicationError()
    data class Remote(val message: String? = null) : CommunicationError()
    data class Unknown(val message: String? = null) : CommunicationError()
}

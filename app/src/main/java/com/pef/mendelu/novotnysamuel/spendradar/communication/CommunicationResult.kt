package com.pef.mendelu.novotnysamuel.spendradar.communication

sealed class CommunicationResult<out T> {
    data class Success<T>(val data: T) : CommunicationResult<T>()
    data class Error(val error: CommunicationError) : CommunicationResult<Nothing>()
}

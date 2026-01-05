package com.pef.mendelu.novotnysamuel.spendradar.utils

import android.content.Context
import android.location.LocationManager

fun getLastKnownLocation(context: Context): android.location.Location? {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        ?: return null
    val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
    return providers.firstNotNullOfOrNull { provider ->
        runCatching { locationManager.getLastKnownLocation(provider) }.getOrNull()
    }
}
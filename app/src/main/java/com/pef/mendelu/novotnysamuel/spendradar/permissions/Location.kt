package com.pef.mendelu.novotnysamuel.spendradar.permissions

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit = {}
) {
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(locationPermissionState.status) {
        when {
            locationPermissionState.status.isGranted -> onPermissionGranted()
            else -> locationPermissionState.launchPermissionRequest()
        }
    }

    if (!locationPermissionState.status.isGranted) {
        onPermissionDenied()
    }
}
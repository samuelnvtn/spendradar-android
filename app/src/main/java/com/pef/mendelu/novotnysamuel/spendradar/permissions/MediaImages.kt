package com.pef.mendelu.novotnysamuel.spendradar.permissions

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MediaPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit = {}
) {
    val mediaPermissionState = rememberPermissionState(Manifest.permission.READ_MEDIA_IMAGES)

    LaunchedEffect(mediaPermissionState.status) {
        when {
            mediaPermissionState.status.isGranted -> onPermissionGranted()
            else -> mediaPermissionState.launchPermissionRequest()
        }
    }

    if (!mediaPermissionState.status.isGranted) {
        onPermissionDenied()
    }
}
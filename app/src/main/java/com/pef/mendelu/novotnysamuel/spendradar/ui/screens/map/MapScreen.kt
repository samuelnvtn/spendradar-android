package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.navigation.Destination
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import com.pef.mendelu.novotnysamuel.spendradar.permissions.LocationPermissionHandler
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.AddReceiptDialog
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BaseScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BottomNavBar
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.accentColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor

@Composable
fun MapScreen(
    navigation: INavigationRouter
){
    val viewModel = hiltViewModel<MapViewModel>()

    val state = viewModel.mapUIState.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadReceipts()
    }

    val receipts = (state.value as? MapUIState.Success)?.receipts.orEmpty()

    if (showDialog) {
        AddReceiptDialog(
            onDismissRequest = { showDialog = false },
            navigation = navigation
        )
    }

    BaseScreen(
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.MapScreen,
                navigation = navigation
            )
        },
        actions = {
            IconButton(onClick = {
                navigation.navigateToSettings()
            }) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = textColor
                )
            }
        },
        topBarText = (stringResource(R.string.map)),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showDialog = true
            }, containerColor = barButton, contentColor = accentColor,
                modifier = Modifier.offset(y = (-16).dp)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_barcode_scanner),
                    contentDescription = null
                )
            }
        },
        content = {
            MapScreenContent(
                paddingValues = it,
                receipts = receipts
            )}
    )

}

@OptIn(MapsComposeExperimentalApi::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreenContent(
    paddingValues: PaddingValues,
    receipts: List<Receipt>
) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(49.2098028, 16.6145219), 10f)
    }

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = true
            )
        )
    }

    var hasLocationPermission by remember { mutableStateOf(false) }

    LocationPermissionHandler(
        onPermissionGranted = { hasLocationPermission = true },
        onPermissionDenied = { hasLocationPermission = false }
    )

    val context = LocalContext.current
    val markerCache = remember { mutableMapOf<String, BitmapDescriptor>() }

    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxHeight(),
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission)
        ) {
            receipts.forEach { receipt ->
                val position = LatLng(receipt.latitude, receipt.longitude)
                val type = receipt.type ?: "OTHER"
                val icon = markerCache.getOrPut(type) {
                    buildMarkerIcon(
                        context = context,
                        iconRes = categoryIcon(type),
                        backgroundColor = categoryColor(type),
                        iconTint = iconTint(type, categoryColor(type))
                    )
                }
                Marker(
                    state = MarkerState(position = position),
                    icon = icon
                )
            }
        }
    }
}

@DrawableRes
private fun categoryIcon(type: String) = when (type) {
    "FOOD_DRINK" -> R.drawable.fork
    "GROCERIES" -> R.drawable.shop
    "PHARMACY" -> R.drawable.medic
    "ELECTRONICS" -> R.drawable.electro
    "FASHION" -> R.drawable.fashion
    else -> R.drawable.other
}

private fun categoryColor(type: String) = when (type) {
    "FOOD_DRINK" -> Color(0xFFB07D22)
    "GROCERIES" -> Color(0xFF1F8F4E)
    "PHARMACY" -> Color(0xFF2A7DA0)
    "ELECTRONICS" -> Color(0xFF4F6CE2)
    "FASHION" -> Color(0xFF8A3E9E)
    else -> Color(0xFF6E6E6E)
}

@Composable
private fun iconTint(type: String, baseColor: Color): Color {
    return when (type) {
        "FOOD_DRINK" -> Color(0xFFEBBF11)
        "GROCERIES" -> accentColor
        else -> lighten(baseColor, 0.55f)
    }
}

private fun lighten(color: Color, factor: Float): Color {
    val clamped = factor.coerceIn(0f, 1f)
    return Color(
        red = color.red + (1f - color.red) * clamped,
        green = color.green + (1f - color.green) * clamped,
        blue = color.blue + (1f - color.blue) * clamped,
        alpha = color.alpha
    )
}

private fun buildMarkerIcon(
    context: Context,
    @DrawableRes iconRes: Int,
    backgroundColor: Color,
    iconTint: Color
): BitmapDescriptor {
    val sizePx = (46 * context.resources.displayMetrics.density).toInt()
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = backgroundColor.toArgb()
    }

    val centerX = sizePx / 2f
    val centerY = sizePx * 0.34f
    val radius = sizePx * 0.32f
    val tipY = sizePx * 0.82f

    val path = Path().apply {
        moveTo(centerX, centerY - radius)
        cubicTo(
            centerX + radius * 1.15f,
            centerY - radius,
            centerX + radius * 1.2f,
            centerY + radius * 0.42f,
            centerX,
            tipY
        )
        cubicTo(
            centerX - radius * 1.2f,
            centerY + radius * 0.42f,
            centerX - radius * 1.15f,
            centerY - radius,
            centerX,
            centerY - radius
        )
        close()
    }

    canvas.drawPath(path, paint)

    val drawable = ContextCompat.getDrawable(context, iconRes)
        ?: return BitmapDescriptorFactory.fromBitmap(bitmap)
    val wrapped = DrawableCompat.wrap(drawable).mutate()
    DrawableCompat.setTint(wrapped, iconTint.toArgb())

    val iconSize = (radius * 1.2f).toInt()
    val left = (centerX - iconSize / 2f).toInt()
    val top = (centerY - iconSize / 2f).toInt()
    wrapped.setBounds(left, top, left + iconSize, top + iconSize)
    wrapped.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

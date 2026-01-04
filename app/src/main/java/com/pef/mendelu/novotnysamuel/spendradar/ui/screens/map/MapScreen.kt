package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.darkGreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor
import java.util.Locale

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

@OptIn(MapsComposeExperimentalApi::class, ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class
)
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
    var selectedReceipt by remember { mutableStateOf<Receipt?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
                    icon = icon,
                    onClick = {
                        selectedReceipt = receipt
                        true
                    }
                )
            }
        }
    }

    if (selectedReceipt != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedReceipt = null },
            sheetState = sheetState,
            containerColor = darkGreen
        ) {
            ReceiptBottomSheetContent(receipt = selectedReceipt!!)
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

@Composable
private fun ReceiptBottomSheetContent(receipt: Receipt) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        val category = receipt.type ?: "OTHER"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = categoryColor(category),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = categoryIcon(category)),
                        contentDescription = null,
                        tint = iconTint(category, categoryColor(category))
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = receipt.storeName,
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = categoryLabel(category),
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.copy(alpha = 0.7f)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.total_label),
            style = MaterialTheme.typography.bodySmall,
            color = textColor.copy(alpha = 0.7f)
        )
        Text(
            text = formatCurrency(receipt.totalPrice),
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (receipt.items.isEmpty()) {
            Text(
                text = stringResource(R.string.no_items),
                style = MaterialTheme.typography.bodySmall,
                color = textColor.copy(alpha = 0.7f)
            )
        } else {
            receipt.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.itemName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor
                        )
                        Text(
                            text = "${item.quantity} ks",
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor.copy(alpha = 0.7f)
                        )
                    }
                    Text(
                        text = formatCurrency(item.pricePerUnit),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun categoryLabel(type: String): String {
    return when (type) {
        "FOOD_DRINK" -> stringResource(R.string.category_food_drink)
        "GROCERIES" -> stringResource(R.string.category_groceries)
        "PHARMACY" -> stringResource(R.string.category_pharmacy)
        "ELECTRONICS" -> stringResource(R.string.category_electronics)
        "FASHION" -> stringResource(R.string.category_fashion)
        else -> stringResource(R.string.category_other)
    }
}

private fun formatCurrency(value: Long): String {
    val absValue = kotlin.math.abs(value)
    val crowns = absValue / 100
    val cents = absValue % 100
    return String.format(Locale("cs", "CZ"), "%d,%02d Kč", crowns, cents)
}

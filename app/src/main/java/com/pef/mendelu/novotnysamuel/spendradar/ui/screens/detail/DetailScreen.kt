package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptCategory
import com.pef.mendelu.novotnysamuel.spendradar.navigation.Destination
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BaseScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BottomNavBar
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.CategoryIconBadge
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.categoryColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.categoryIcon
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.categoryIconTint
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.customMarkerIcon
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.formatCurrency
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.basicMargin
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.darkGreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.halfMargin
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.ultraGreen
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DetailScreen(
    navigation: INavigationRouter,
    id: Long
){
    val viewModel = hiltViewModel<DetailViewModel>()

    val state = viewModel.detailUIState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.loadReceipt(id)
    }

    LaunchedEffect(state.value) {
        if (state.value is DetailUIState.Deleted) {
            navigation.navigateToList()
        }
    }

    BaseScreen(
        actions = {
            IconButton(onClick = {
                val editId = (state.value as? DetailUIState.Success)?.receipt?.id
                if (editId != null) {
                    navigation.navigateToAdd(editId)
                }
            }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit",
                    tint = textColor
                )
            }},
        topBarText = stringResource(R.string.detail),
        onBackClick = { navigation.returnBack() },
        showLoading = state.value is DetailUIState.Loading,
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.ListScreen,
                navigation = navigation
            )
        }
    ) { paddingValues ->
        when (val currentState = state.value) {
            is DetailUIState.Success -> {
                DetailScreenContent(
                    paddingValues = paddingValues,
                    receipt = currentState.receipt,
                    onDelete = { viewModel.deleteReceipt(currentState.receipt) }
                )
            }
            is DetailUIState.Error -> {
                Text(
                    text = currentState.message ?: stringResource(R.string.error_item_name_required),
                    color = textColor,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            else -> {}
        }
    }

}

@Composable
fun DetailScreenContent(
    paddingValues: PaddingValues,
    receipt: Receipt,
    onDelete: () -> Unit
){
    val category = ReceiptCategory.fromStorage(receipt.type)
    val formatter = remember {
        DateTimeFormatter.ofPattern("d. MMMM yyyy, HH:mm", Locale.getDefault())
    }
    val formattedDate = remember(receipt.timestamp) {
        receipt.timestamp.atZone(ZoneId.systemDefault()).format(formatter)
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(scrollState)
            .padding(horizontal = basicMargin, vertical = halfMargin)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = ultraGreen,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.total_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "-${formatCurrency(receipt.totalPrice)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = receipt.storeName,
                        style = MaterialTheme.typography.titleLarge,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor.copy(alpha = 0.7f)
                    )
                }
                CategoryIconBadge(
                    category = category,
                    size = 56.dp,
                    shape = CircleShape
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.purchase_location),
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
        Spacer(modifier = Modifier.height(10.dp))
        ReceiptStaticMap(
            latitude = receipt.latitude,
            longitude = receipt.longitude,
            category = category
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.purchase_items),
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = ultraGreen,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (receipt.items.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_items),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor.copy(alpha = 0.7f)
                    )
                } else {
                    receipt.items.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.itemName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = textColor,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${item.quantity}x",
                                style = MaterialTheme.typography.bodySmall,
                                color = textColor.copy(alpha = 0.7f),
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Text(
                                text = formatCurrency(item.pricePerUnit),
                                style = MaterialTheme.typography.bodyMedium,
                                color = textColor
                            )
                        }
                        if (index != receipt.items.lastIndex) {
                            Divider(color = barButton.copy(alpha = 0.4f))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(26.dp))
        OutlinedButton(
            onClick = onDelete,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = androidx.compose.ui.graphics.Color.Red
            ),
            border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color.Red)
        ) {
            Text(
                text = stringResource(R.string.delete_entry),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
private fun ReceiptStaticMap(
    latitude: Double,
    longitude: Double,
    category: ReceiptCategory
) {
    val position = rememberCameraPositionState {
        val latLng = LatLng(latitude, longitude)
        position = CameraPosition.fromLatLngZoom(latLng, 16f)
    }
    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = false,
            scrollGesturesEnabled = false,
            zoomGesturesEnabled = false,
            tiltGesturesEnabled = false,
            rotationGesturesEnabled = false
        )
    }
    val context = androidx.compose.ui.platform.LocalContext.current
    val baseColor = categoryColor(category)
    val markerIcon = remember(category) {
        customMarkerIcon(
            context = context,
            iconRes = categoryIcon(category),
            backgroundColor = baseColor,
            iconTint = categoryIconTint(category, baseColor)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = position,
            uiSettings = mapUiSettings,
            properties = MapProperties(isMyLocationEnabled = false)
        ) {
            Marker(
                state = MarkerState(position = LatLng(latitude, longitude)),
                icon = markerIcon,
                onClick = { true }
            )
        }
    }
}

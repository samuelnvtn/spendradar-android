package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.pef.mendelu.novotnysamuel.spendradar.BuildConfig
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.map.ClusterMapRenderer
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.navigation.Destination
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import com.pef.mendelu.novotnysamuel.spendradar.permissions.LocationPermissionHandler
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagMapOpenFirstReceipt
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.AddReceiptDialog
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BaseScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BottomNavBar
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.ReceiptBottomSheetContent
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.accentColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.darkGreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor
import kotlinx.coroutines.launch

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
    val isLoading = state.value is MapUIState.Loading

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
                receipts = receipts,
                isLoading = isLoading,
                navigation = navigation
            )}
    )

}

@OptIn(MapsComposeExperimentalApi::class, ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun MapScreenContent(
    paddingValues: PaddingValues,
    receipts: List<Receipt>,
    isLoading: Boolean,
    navigation: INavigationRouter
) {

    var clusterManager by remember { mutableStateOf<ClusterManager<Receipt>?>(null) }
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
    var selectedReceipt by remember { mutableStateOf<Receipt?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

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
            MapEffect { map ->
                if (clusterManager == null){
                    clusterManager = ClusterManager<Receipt>(context, map)
                }

                clusterManager?.apply {
                    renderer = ClusterMapRenderer(context, map, this)
                    setOnClusterItemClickListener { item ->
                        selectedReceipt = item
                        true
                    }
                    setOnClusterClickListener { cluster ->
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.position, map.cameraPosition.zoom + 2))
                        true
                    }
                }

                clusterManager?.clearItems()
                clusterManager?.addItems(receipts)
                clusterManager?.cluster()
                map.setOnCameraIdleListener {
                    clusterManager?.cluster()
                }

            }
        }
        if (BuildConfig.DEBUG && receipts.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(1.dp)
                    .testTag(TestTagMapOpenFirstReceipt)
                    .clickable { selectedReceipt = receipts.first() }
            )
        }
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = accentColor)
            }
        }
    }

    if (selectedReceipt != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedReceipt = null },
            sheetState = sheetState,
            containerColor = darkGreen
        ) {
            ReceiptBottomSheetContent(
                receipt = selectedReceipt!!,
                onDetailClick = {
                    val id = selectedReceipt?.id ?: return@ReceiptBottomSheetContent
                    scope.launch {
                        sheetState.hide()
                        selectedReceipt = null
                        navigation.navigateToDetail(id)
                    }
                }
            )
        }
    }
}

package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.navigation.Destination
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import com.pef.mendelu.novotnysamuel.spendradar.permissions.LocationPermissionHandler
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BaseScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BottomNavBar
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.OptionButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.accentColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor

@Composable
fun MapScreen(
    navigation: INavigationRouter
){
    val viewModel = hiltViewModel<MapViewModel>()

    val state = viewModel.mapUIState.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = barButton,
                border = BorderStroke(1.dp, barColor),
                modifier = Modifier.width(260.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.add_receipt),
                        color = textColor,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)
                    )

                    OptionButton(
                        painter = painterResource(id = R.drawable.camera),
                        label = stringResource(R.string.scan_camera),
                        onClick = {
                            showDialog = false
                            // TODO: Camera scan
                        }
                    )
                    /* OptionButton(
                         painter = painterResource(id = R.drawable.insert_photo),
                         label = stringResource(R.string.scan_gallery),
                         onClick = {
                             showDialog = false
                             // TODO: Gallery scan
                         }
                     )*/
                    OptionButton(
                        painter = painterResource(id = R.drawable.edit),
                        label = stringResource(R.string.manual_entry),
                        onClick = {
                            showDialog = false
                            // TODO: Manual entry
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = { showDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = stringResource(R.string.cancel), color = textColor)
                    }
                }
            }
        }
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
                
            )}
    )

}

@OptIn(MapsComposeExperimentalApi::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreenContent(
    paddingValues: PaddingValues,
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
            
        }
    }
}
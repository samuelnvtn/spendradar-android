package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.camera

import com.pef.mendelu.novotnysamuel.spendradar.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pef.mendelu.novotnysamuel.spendradar.communication.CommunicationError
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import com.pef.mendelu.novotnysamuel.spendradar.permissions.CameraPermissionHandler
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BaseScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.CameraComposeView
import com.pef.mendelu.novotnysamuel.spendradar.utils.TextRecognizer
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.accentColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor


@Composable
fun TextRecognitionScreen(navigation: INavigationRouter) {
    val viewModel = hiltViewModel<TextRecognitionViewModel>()
    val navigateToEditId by viewModel.navigateToEditId.collectAsStateWithLifecycle()

    LaunchedEffect(navigateToEditId) {
        val id = navigateToEditId
        if (id != null) {
            navigation.navigateToAdd(fromCamera = true)
            viewModel.onNavigationHandled()
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.receipt_scanner_title),
        onBackClick = { navigation.returnBack() },
        showBottomBar = false
    ) {
        TextRecognitionScreenContent(
            paddingValues = it,
            viewModel = viewModel
        )
    }
}

@Composable
fun TextRecognitionScreenContent(
    paddingValues: PaddingValues,
    viewModel: TextRecognitionViewModel
) {
    val isCapturing by viewModel.isCapturing.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    var hasPermission by remember { mutableStateOf(false) }
    var torchEnabled by remember { mutableStateOf(false) }

    val textAnalyzer = remember(viewModel) {
        TextRecognizer(
            shouldAnalyze = { viewModel.isCapturing.value },
            onImageCaptured = { viewModel.onImageCaptured(it) }
        )
    }

    CameraPermissionHandler(
        onPermissionGranted = { hasPermission = true },
        onPermissionDenied = { hasPermission = false }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (hasPermission) {
            CameraComposeView(
                paddingValues = PaddingValues(0.dp),
                analyzer = textAnalyzer,
                torchEnabled = torchEnabled
            ) { }
        }

        // Loading Overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = accentColor)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.gemini_analyzing),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (error != null) {
            val message = when (error) {
                is CommunicationError.NoInternet -> stringResource(R.string.gemini_no_internet)
                else -> stringResource(R.string.gemini_failed)
            }
            AlertDialog(
                onDismissRequest = { viewModel.onErrorHandled() },
                title = { Text(text = stringResource(R.string.error_title)) },
                text = { Text(text = message) },
                confirmButton = {
                    Button(
                        onClick = { viewModel.onErrorHandled() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = textColor
                        )
                    ) {
                        Text(text = stringResource(R.string.action_ok))
                    }
                },
                containerColor = barButton,
                titleContentColor = textColor,
                textContentColor = textColor,
                tonalElevation = AlertDialogDefaults.TonalElevation
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            IconButton(
                onClick = { viewModel.startCapture() },
                enabled = !isLoading && !isCapturing,
                modifier = Modifier
                    .size(64.dp)
                    .background(color = accentColor.copy(alpha = 0.55f), shape = CircleShape)
            ) {
            }
        }

        if (hasPermission) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, end = 16.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                val icon: ImageVector = if (torchEnabled) {
                    Icons.Filled.FlashOn
                } else {
                    Icons.Filled.FlashOff
                }
                IconButton(
                    onClick = { torchEnabled = !torchEnabled },
                    enabled = !isLoading,
                    modifier = Modifier
                        .size(48.dp)
                        .background(color = barButton.copy(alpha = 0.7f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (torchEnabled) {
                            stringResource(R.string.action_flash_off)
                        } else {
                            stringResource(R.string.action_flash_on)
                        },
                        tint = textColor
                    )
                }
            }
        }
    }
}

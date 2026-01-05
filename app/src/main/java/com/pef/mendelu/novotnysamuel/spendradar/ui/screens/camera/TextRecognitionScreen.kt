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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import com.pef.mendelu.novotnysamuel.spendradar.permissions.CameraPermissionHandler
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BaseScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.CameraComposeView
import com.pef.mendelu.novotnysamuel.spendradar.utils.TextRecognizer
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.accentColor


@Composable
fun TextRecognitionScreen(navigation: INavigationRouter) {
    val viewModel = hiltViewModel<TextRecognitionViewModel>()
    val navigateToEditId by viewModel.navigateToEditId.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(navigateToEditId) {
        val id = navigateToEditId
        if (id != null) {
            navigation.navigateToAdd(id, fromCamera = true)
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
    var hasPermission by remember { mutableStateOf(false) }

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

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        if (hasPermission) {
            CameraComposeView(
                paddingValues = PaddingValues(0.dp),
                analyzer = textAnalyzer
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
    }
}

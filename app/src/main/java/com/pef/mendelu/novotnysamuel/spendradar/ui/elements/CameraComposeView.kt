package cz.pef.mendelu.mlkit2024.ui.elements

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executors

@Composable
fun CameraComposeView(
    paddingValues: PaddingValues,
    analyzer: ImageAnalysis.Analyzer,
    overlay: @Composable () -> Unit
    ) {

    // Get the current Android context
    val context = LocalContext.current

// Obtain a ProcessCameraProvider to manage camera lifecycle
    val processCameraProvider = remember { ProcessCameraProvider.getInstance(context) }

// Create a single-threaded executor for camera operations
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

// Get the main thread executor for UI updates
    val mainExecutor = ContextCompat.getMainExecutor(context)

// Retrieve the ProcessCameraProvider instance (blocking call)
    val cameraProvider = processCameraProvider.get()

// Get the current LifecycleOwner for managing lifecycle events
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

// Hold a mutable state for the camera preview
    var preview by remember { mutableStateOf<Preview?>(null) }

    Box(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = { context ->
                    val previewView = PreviewView(context)
                    processCameraProvider.addListener({
                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()
                        cameraProvider.unbindAll()

                        val analyzerBuilder = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        analyzerBuilder.setAnalyzer(cameraExecutor, analyzer)

                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            analyzerBuilder,
                            preview
                        )
                    }, mainExecutor)
                    preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    previewView
                }
            )
        }
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            overlay()
        }
    }
}
package bob.colbaskin.umir_hack_2.scanner.presentation.camera

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import bob.colbaskin.umir_hack_2.scanner.presentation.utils.DiplomaQrTokenParser
import io.scanbot.sdk.barcode.BarcodeFormat
import io.scanbot.sdk.ui_v2.common.OrientationLockMode
import io.scanbot.sdk.ui_v2.barcode.BarcodeScannerView
import io.scanbot.sdk.ui_v2.barcode.configuration.BarcodeScannerScreenConfiguration
import io.scanbot.sdk.ui_v2.common.ScanbotColor

@Composable
fun CameraScannerView(
    modifier: Modifier = Modifier,
    sessionId: Long,
    onQrScanned: (String) -> Unit,
    onClosed: () -> Unit,
) {
    val screenConfiguration = remember {
        BarcodeScannerScreenConfiguration().apply {
            cameraConfiguration.apply {
                flashEnabled = false
                orientationLockMode = OrientationLockMode.PORTRAIT
            }

            scannerConfiguration.apply { barcodeFormats = listOf(BarcodeFormat.QR_CODE) }

            actionBar.flipCameraButton.visible = false
            actionBar.zoomButton.visible = false
            actionBar.flashButton.visible = false

            topBar.title.visible = false
            topBar.cancelButton.visible = false
            topBar.backgroundColor = ScanbotColor(Color.Transparent)

            userGuidance.visible = false
            vibration.enabled = true
        }
    }

    key(sessionId) {
        BarcodeScannerView(
            modifier = modifier.fillMaxSize(),
            configuration = screenConfiguration,
            onBarcodeScanned = { result ->
                Log.d("SCAN", "onBarcodeScanned: ${result.items.firstOrNull()?.barcode?.text}")
                val barcodeItem = result.items.firstOrNull() ?: return@BarcodeScannerView
                val raw = barcodeItem.barcode.text.trim()
                if (raw.isEmpty()) return@BarcodeScannerView

                val token = DiplomaQrTokenParser.extractTokenOrNull(raw)
                    ?: return@BarcodeScannerView

                onQrScanned(token)
            },
            onBarcodeScannerClosed = { reason  ->
                Log.d("SCAN", "onBarcodeScannerClosed: $reason")
                onClosed()
            }
        )
    }
}

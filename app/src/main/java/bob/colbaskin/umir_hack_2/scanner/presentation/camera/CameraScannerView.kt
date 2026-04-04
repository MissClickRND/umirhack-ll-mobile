package bob.colbaskin.umir_hack_2.scanner.presentation.camera

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

            scannerConfiguration.apply {
                barcodeFormats = listOf(
                    BarcodeFormat.QR_CODE,
                    BarcodeFormat.CODABAR,
                    BarcodeFormat.CODE_128,
                    BarcodeFormat.CODE_39,
                    BarcodeFormat.EAN_13
                )
            }

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
                val barcodeItem = result.items.firstOrNull() ?: return@BarcodeScannerView
                val qrText = barcodeItem.barcode.text.trim()
                if (qrText.isNotEmpty()) {
                    onQrScanned(qrText)
                }
            },
            onBarcodeScannerClosed = { _ ->
                onClosed()
            }
        )
    }
}

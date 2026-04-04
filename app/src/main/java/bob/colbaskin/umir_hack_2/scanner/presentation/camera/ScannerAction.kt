package bob.colbaskin.umir_hack_2.scanner.presentation.camera

import android.net.Uri

sealed interface ScannerAction {
    data object RequestCameraPermission : ScannerAction
    data class OnCameraPermissionResult(val granted: Boolean) : ScannerAction
    data object StartCameraScan : ScannerAction
    data object StopCameraScan : ScannerAction
    data class OnHashScanned(val hash: String) : ScannerAction
    data class OnFileSelected(val uri: Uri) : ScannerAction
    data object ClearSelectedFile : ScannerAction
    data object CheckDocument : ScannerAction
    data object ClearDocumentResult : ScannerAction
}

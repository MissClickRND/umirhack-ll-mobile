package bob.colbaskin.umir_hack_2.scanner.presentation.camera

import bob.colbaskin.umir_hack_2.scanner.domain.models.CameraPermissionState

data class ScannerState(
    val cameraPermissionState: CameraPermissionState = CameraPermissionState.NOT_REQUESTED,
    val isCameraScanning: Boolean = false,
    val scanSessionId: Long = 0L,
    val selectedFileUri: String? = null,
)

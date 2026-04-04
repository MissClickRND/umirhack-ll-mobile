package bob.colbaskin.umir_hack_2.scanner.presentation.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import bob.colbaskin.umir_hack_2.scanner.domain.models.CameraPermissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class ScannerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    var state by mutableStateOf(ScannerState())
        private set

    fun onAction(action: ScannerAction) {
        when (action) {
            is ScannerAction.RequestCameraPermission -> requestCameraPermission()
            is ScannerAction.OnCameraPermissionResult -> onCameraPermissionResult(action.granted)
            is ScannerAction.StartCameraScan -> startCameraScan()
            is ScannerAction.StopCameraScan -> stopCameraScan()
            is ScannerAction.OnFileSelected -> onFileSelected(action.uri)
            is ScannerAction.ClearSelectedFile -> clearSelectedFile()

            else -> Unit
        }
    }

    private fun requestCameraPermission() {
        val permissionStatus = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )

        state = if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            state.copy(
                cameraPermissionState = CameraPermissionState.GRANTED,
                isCameraScanning = true,
                scanSessionId = state.scanSessionId + 1
            )
        } else {
            state.copy(cameraPermissionState = CameraPermissionState.NOT_GRANTED)
        }
    }

    private fun onCameraPermissionResult(granted: Boolean) {
        state = if (granted) {
            state.copy(
                cameraPermissionState = CameraPermissionState.GRANTED,
                isCameraScanning = true,
                scanSessionId = state.scanSessionId + 1
            )
        } else {
            state.copy(
                cameraPermissionState = CameraPermissionState.DENIED,
                isCameraScanning = false
            )
        }
    }

    private fun startCameraScan() {
        if (state.cameraPermissionState == CameraPermissionState.GRANTED) {
            state = state.copy(
                isCameraScanning = true,
                scanSessionId = state.scanSessionId + 1
            )
        }
    }

    private fun stopCameraScan() {
        state = state.copy(isCameraScanning = false)
    }

    private fun onFileSelected(uri: Uri) {
        state = state.copy(
            selectedFileUri = uri.toString(),
            isCameraScanning = false
        )
    }

    private fun clearSelectedFile() {
        state = state.copy(selectedFileUri = null)
    }
}

package bob.colbaskin.umir_hack_2.scanner.presentation.camera

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.umir_hack_2.scanner.domain.models.CameraPermissionState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import com.google.accompanist.permissions.PermissionState
import bob.colbaskin.umir_hack_2.navigation.NavResultKeys
import bob.colbaskin.umir_hack_2.navigation.Screens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.Photo
import compose.icons.tablericons.Refresh

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreenRoot(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    LaunchedEffect(cameraPermissionState.status) {
        val granted = cameraPermissionState.status is PermissionStatus.Granted
        viewModel.onAction(ScannerAction.OnCameraPermissionResult(granted))
    }

    LaunchedEffect(state.selectedFileUri) {
        state.selectedFileUri?.let { uri ->
            navController.navigate(Screens.ImageAreaSelectionScreen(uri))
            viewModel.onAction(ScannerAction.ClearSelectedFile)
        }
    }

    ScannerScreen(
        state = state,
        onAction = viewModel::onAction,
        cameraPermissionState = cameraPermissionState,
        onClose = { navController.popBackStack() },
        onQrScanned = { qrText ->
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(NavResultKeys.QR_TEXT, qrText)

            viewModel.onAction(ScannerAction.StopCameraScan)
            navController.popBackStack()
        }
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    state: ScannerState,
    onAction: (ScannerAction) -> Unit,
    cameraPermissionState: PermissionState,
    onClose: () -> Unit,
    onQrScanned: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        if (state.cameraPermissionState == CameraPermissionState.GRANTED && state.isCameraScanning) {
            CameraScannerView(
                sessionId = state.scanSessionId,
                onQrScanned = onQrScanned,
                onClosed = {
                    onAction(ScannerAction.StopCameraScan)
                    onClose()
                }
            )
        } else {
            Box(Modifier.fillMaxSize().background(Color.Black))
        }

        ScannerOverlay(
            state = state,
            onAction = onAction,
            cameraPermissionState = cameraPermissionState,
            onClose = onClose
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ScannerOverlay(
    state: ScannerState,
    onAction: (ScannerAction) -> Unit,
    cameraPermissionState: PermissionState,
    onClose: () -> Unit
) {
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { onAction(ScannerAction.OnFileSelected(it)) } }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .padding(top = 18.dp, start = 14.dp)
                .size(44.dp)
                .background(CustomTheme.colors.buttonSecondaryBackground, CircleShape)
        ) {
            Icon(
                imageVector = TablerIcons.ArrowLeft,
                contentDescription = "Назад",
                tint = CustomTheme.colors.textPrimary
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundActionButton(
                icon = TablerIcons.Photo,
                contentDescription = "Открыть из галереи",
                onClick = {
                    pickMediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
            Spacer(modifier = Modifier.width(20.dp))
            RoundActionButton(
                icon = TablerIcons.Refresh,
                contentDescription = "Перезапустить сканер",
                onClick = {
                    if (state.cameraPermissionState == CameraPermissionState.GRANTED) {
                        onAction(ScannerAction.StartCameraScan)
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                }
            )
        }

        if (state.cameraPermissionState != CameraPermissionState.GRANTED) {
            CameraPermissionOverlay(
                onRequestPermission = { cameraPermissionState.launchPermissionRequest() }
            )
        }
    }
}

@Composable
private fun RoundActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(CustomTheme.colors.buttonSecondaryBackground, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = CustomTheme.colors.textPrimary,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun CameraPermissionOverlay(onRequestPermission: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "Требуется доступ к камере",
                style = CustomTheme.typography.inter.titleMedium,
                color = CustomTheme.colors.textPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = onRequestPermission,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.buttonSecondaryBackground,
                    contentColor = CustomTheme.colors.buttonSecondaryContent
                )
            ) {
                Text("Разрешить доступ к камере")
            }
        }
    }
}

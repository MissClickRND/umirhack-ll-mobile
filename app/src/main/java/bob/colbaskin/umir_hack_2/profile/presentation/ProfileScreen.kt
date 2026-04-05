package bob.colbaskin.umir_hack_2.profile.presentation

import android.content.ClipData
import android.content.Intent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import bob.colbaskin.umir_hack_2.common.UiState
import bob.colbaskin.umir_hack_2.common.design_system.ErrorScreen
import bob.colbaskin.umir_hack_2.common.design_system.LoadingScreen
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import bob.colbaskin.umir_hack_2.navigation.Screens
import bob.colbaskin.umir_hack_2.profile.presentation.components.FullScreenQrDialog
import bob.colbaskin.umir_hack_2.profile.presentation.components.ProfileContent
import bob.colbaskin.umir_hack_2.profile.presentation.components.ShareDiplomaBottomSheet

@Composable
fun ProfileScreenRoot(
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(
            androidx.lifecycle.Lifecycle.State.STARTED
        ) {
            viewModel.onAction(ProfileAction.RefreshAll)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ProfileEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.text)

                is ProfileEffect.CopyToClipboard -> {
                    val clipboard = context.getSystemService(android.content.ClipboardManager::class.java)
                    val clip = ClipData.newPlainText(effect.label, effect.value)
                    clipboard.setPrimaryClip(clip)
                }

                is ProfileEffect.ShareText -> {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, effect.value)
                        type = "text/plain"
                    }
                    val chooser = Intent.createChooser(sendIntent, null)
                    context.startActivity(chooser)
                }

                ProfileEffect.NavigateToDiplomaCheck -> {
                    navController.navigate(Screens.DiplomaCheck)
                }
            }
        }
    }

    when (val userState = state.userState) {
        UiState.Loading -> {
            LoadingScreen(onError = { viewModel.onAction(ProfileAction.LoadUser) })
        }
        is UiState.Error -> {
            ErrorScreen(
                message = userState.text,
                onError = { viewModel.onAction(ProfileAction.LoadUser) }
            )
        }
        is UiState.Success<User> -> {
            ProfileContent(
                user = userState.data,
                state = state,
                onAction = viewModel::onAction
            )
        }
    }

    if (state.isShareSheetVisible) {
        ShareDiplomaBottomSheet(state = state, onAction = viewModel::onAction)
    }

    state.fullscreenQrLink?.let { link ->
        FullScreenQrDialog(
            link = link,
            onDismiss = { viewModel.onAction(ProfileAction.HideQrFullscreen) }
        )
    }
}

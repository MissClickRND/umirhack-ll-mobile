package bob.colbaskin.umir_hack_2.profile.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import bob.colbaskin.umir_hack_2.common.UiState
import bob.colbaskin.umir_hack_2.common.design_system.ErrorScreen
import bob.colbaskin.umir_hack_2.common.design_system.LoadingScreen
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import bob.colbaskin.umir_hack_2.profile.presentation.components.FullScreenQrDialog
import bob.colbaskin.umir_hack_2.profile.presentation.components.ProfileContent
import bob.colbaskin.umir_hack_2.profile.presentation.components.ShareDiplomaBottomSheet

@Composable
fun ProfileScreenRoot(
    snackbarHostState: SnackbarHostState,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.onAction(ProfileAction.ClearMessage)
        }
    }

    ProfileScreen(
        state = state,
        onAction = viewModel::onAction
    )

    if (state.isShareSheetVisible) {
        ShareDiplomaBottomSheet(
            state = state,
            onAction = viewModel::onAction
        )
    }

    state.fullscreenQrLink?.let { link ->
        FullScreenQrDialog(
            link = link,
            onDismiss = { viewModel.onAction(ProfileAction.HideQrFullscreen) }
        )
    }
}

@Composable
private fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    when (val user = state.userState) {
        is UiState.Error -> {
            ErrorScreen(
                message = user.text,
                onError = { onAction(ProfileAction.LoadUser) }
            )
        }

        is UiState.Success<User> -> {
            ProfileContent(
                user = user.data,
                state = state,
                onAction = onAction
            )
        }

        UiState.Loading -> {
            LoadingScreen(
                onError = { onAction(ProfileAction.LoadUser) }
            )
        }
    }
}

package bob.colbaskin.umir_hack_2.auth.presentation.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.umir_hack_2.auth.presentation.components.AuthScreenContainer
import bob.colbaskin.umir_hack_2.auth.presentation.components.AuthTextField
import bob.colbaskin.umir_hack_2.common.UiState
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.navigation.Screens
import bob.colbaskin.umir_hack_2.navigation.graphs.Graphs

@Composable
fun SignUpScreenRoot(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val authState = state.authState

    LaunchedEffect(authState) {
        if (authState is UiState.Error) {
            snackbarHostState.showSnackbar(
                authState.title,
                duration = SnackbarDuration.Short
            )
            viewModel.resetAuthState()
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is UiState.Error -> {
                snackbarHostState.showSnackbar(
                    authState.title,
                    duration = SnackbarDuration.Short
                )
                viewModel.resetAuthState()
            }

            is UiState.Success<*> -> {
                navController.navigate(Graphs.Main) {
                    popUpTo(Screens.SignUp) { inclusive = true }
                    launchSingleTop = true
                }
            }

            else -> Unit
        }
    }

    SignUpScreen(
        state = state,
        onAction = { action ->
            when (action) {
                SignUpAction.SignIn -> navController.navigate(Screens.SignIn)
                SignUpAction.NavigateBack -> navController.navigate(Screens.DiplomaCheck)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun SignUpScreen(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
) {
    val colors = CustomTheme.colors
    var showPassword by remember { mutableStateOf(false) }

    AuthScreenContainer(
        title = "Регистрация",
        subtitle = "Создайте профиль сотрудника или работодателя для безопасной проверки подлинности дипломов и цифровых сертификатов.",
        onBackClick = { onAction(SignUpAction.NavigateBack) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            AuthTextField(
                value = state.email,
                onValueChange = { onAction(SignUpAction.UpdateEmail(it)) },
                label = "Email",
                isError = !state.isEmailValid
            )
            AuthTextField(
                value = state.password,
                onValueChange = { onAction(SignUpAction.UpdatePassword(it)) },
                label = "Пароль",
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = null,
                            tint = colors.textSecondary
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = { onAction(SignUpAction.SignUp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !state.isLoading,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.textOnPrimary,
                    disabledContainerColor = colors.primary.copy(alpha = 0.5f),
                    disabledContentColor = colors.textOnPrimary
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = colors.textOnPrimary
                    )
                } else {
                    Text(
                        text = "Зарегистрироваться",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            TextButton(
                onClick = { onAction(SignUpAction.SignIn) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "У меня уже есть аккаунт",
                    color = colors.textSecondary
                )
            }
        }
    }
}

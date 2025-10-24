package bob.colbaskin.hack_template.auth.presentation.sign_in

import android.util.Patterns
import bob.colbaskin.hack_template.common.UiState

data class SignInState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val authState: UiState<Unit> = UiState.Loading
) {
    val isEmailValid: Boolean
        get() = email.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

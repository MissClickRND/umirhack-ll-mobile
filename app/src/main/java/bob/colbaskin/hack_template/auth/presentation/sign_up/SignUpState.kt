package bob.colbaskin.hack_template.auth.presentation.sign_up

import android.util.Patterns
import bob.colbaskin.hack_template.common.UiState
import bob.colbaskin.hack_template.profile.domain.models.User

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val authState: UiState<Unit> = UiState.Loading
) {
    val isEmailValid: Boolean
        get() = email.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

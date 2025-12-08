package bob.colbaskin.hack_template.profile.presentation

import bob.colbaskin.hack_template.common.UiState
import bob.colbaskin.hack_template.profile.domain.models.User

data class ProfileState (
    val userState: UiState<User> = UiState.Loading
)

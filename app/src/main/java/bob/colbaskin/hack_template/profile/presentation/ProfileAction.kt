package bob.colbaskin.hack_template.profile.presentation

sealed interface ProfileAction {
    data object LoadUser: ProfileAction
    data object Logout: ProfileAction
}

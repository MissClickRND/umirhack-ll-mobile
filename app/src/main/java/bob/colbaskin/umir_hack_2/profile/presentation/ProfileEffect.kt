package bob.colbaskin.umir_hack_2.profile.presentation

sealed interface ProfileEffect {
    data class ShowSnackbar(val text: String): ProfileEffect

    data class CopyToClipboard(
        val label: String,
        val value: String
    ) : ProfileEffect

    data class ShareText(val value: String): ProfileEffect

    data object NavigateToDiplomaCheck: ProfileEffect
}

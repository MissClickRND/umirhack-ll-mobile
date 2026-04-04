package bob.colbaskin.umir_hack_2.scanner.presentation.image_selection

sealed interface ImageAreaSelectionAction {
    data class SetImageUri(val uri: String) : ImageAreaSelectionAction
    data object StartDetection : ImageAreaSelectionAction
    data object RetryDetection : ImageAreaSelectionAction
    data object ClearError : ImageAreaSelectionAction
    data object Cancel : ImageAreaSelectionAction
}

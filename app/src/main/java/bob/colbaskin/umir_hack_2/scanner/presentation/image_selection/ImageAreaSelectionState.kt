package bob.colbaskin.umir_hack_2.scanner.presentation.image_selection

data class ImageAreaSelectionState(
    val imageUri: String? = null,
    val isLoading: Boolean = false,
    val detectedQrText: String? = null,
    val errorMessage: String? = null,
    val isCompleted: Boolean = false,
)

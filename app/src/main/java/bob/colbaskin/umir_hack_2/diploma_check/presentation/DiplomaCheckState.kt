package bob.colbaskin.umir_hack_2.diploma_check.presentation

import androidx.compose.ui.text.input.TextFieldValue
import bob.colbaskin.umir_hack_2.common.UiState
import bob.colbaskin.umir_hack_2.profile.domain.models.Diploma
import bob.colbaskin.umir_hack_2.scanner.domain.models.DocumentStatus

data class DiplomaCheckState(
    val diplomaInput: TextFieldValue = TextFieldValue(""),
    val fullNameInput: TextFieldValue = TextFieldValue(""),
    val isLoading: Boolean = false,
    val infoMessage: String? = null,
    val qrCheckLoading: Boolean = false,
    val qrStatus: DocumentStatus = DocumentStatus.NOT_SCANNED,
    val qrError: String? = null,
    val qrRawText: String? = null,
    val numDiploma: Diploma? = null,
    val isAuthorized: Boolean = false,
    val diplomaResult: UiState<Diploma> = UiState.Loading
) {
    val diplomaDigits: String
        get() = diplomaInput.text.filter { it.isDigit() }

    val fullName: String
        get() = fullNameInput.text.trim()

    val canVerify: Boolean
        get() = diplomaDigits.length == 13 && fullName.isNotBlank()
}

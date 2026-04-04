package bob.colbaskin.umir_hack_2.diploma_check.presentation

import androidx.compose.ui.text.input.TextFieldValue
import bob.colbaskin.umir_hack_2.scanner.domain.models.DocumentStatus

data class DiplomaCheckState(
    val diplomaInput: TextFieldValue = TextFieldValue(""),
    val isLoading: Boolean = false,
    val infoMessage: String? = null,
    val qrCheckLoading: Boolean = false,
    val qrStatus: DocumentStatus = DocumentStatus.NOT_SCANNED,
    val qrError: String? = null,
    val qrRawText: String? = null,
    val isAuthorized: Boolean = false,
) {
    val diplomaDigits: String
        get() = diplomaInput.text.filter { it.isDigit() }

    val canVerify: Boolean
        get() = diplomaDigits.length == 13
}

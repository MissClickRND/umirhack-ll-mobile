package bob.colbaskin.umir_hack_2.diploma_check.presentation

import bob.colbaskin.umir_hack_2.scanner.domain.models.DocumentStatus

data class DiplomaCheckState(
    val query: String = "",
    val isLoading: Boolean = false,
    val infoMessage: String? = null,

    val qrCheckLoading: Boolean = false,
    val qrStatus: DocumentStatus = DocumentStatus.NOT_SCANNED,
    val qrError: String? = null,
    val qrRawText: String? = null
) {
    val canVerify: Boolean
        get() = query.isNotBlank() && !isLoading
}

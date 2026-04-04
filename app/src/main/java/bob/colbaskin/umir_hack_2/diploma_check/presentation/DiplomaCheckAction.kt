package bob.colbaskin.umir_hack_2.diploma_check.presentation

import androidx.compose.ui.text.input.TextFieldValue

sealed interface DiplomaCheckAction {
    data class UpdateQuery(val value: TextFieldValue) : DiplomaCheckAction
    data object VerifyDiploma : DiplomaCheckAction
    data object OpenHowItWorks : DiplomaCheckAction
    data object ClearMessage : DiplomaCheckAction
    data class OnQrScanned(val qrText: String) : DiplomaCheckAction
    data object ClearQrResult : DiplomaCheckAction
    data object OpenQrScanner : DiplomaCheckAction
    data object OpenSignIn : DiplomaCheckAction
    data object RefreshAuthStatus : DiplomaCheckAction
}

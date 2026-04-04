package bob.colbaskin.umir_hack_2.diploma_check.presentation

sealed interface DiplomaCheckAction {
    data class UpdateQuery(val value: String) : DiplomaCheckAction

    data object VerifyDiploma : DiplomaCheckAction
    data object OpenQrScanner : DiplomaCheckAction
    data object OpenHowItWorks : DiplomaCheckAction
    data object OpenSignIn : DiplomaCheckAction

    data object ClearMessage : DiplomaCheckAction

    data class OnQrScanned(val qrText: String) : DiplomaCheckAction
    data object ClearQrResult : DiplomaCheckAction
}

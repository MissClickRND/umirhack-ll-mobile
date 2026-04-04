package bob.colbaskin.umir_hack_2.navigation

import kotlinx.serialization.Serializable

interface Screens {
    @Serializable
    data object DiplomaCheck: Screens
    @Serializable
    data object QrScanner: Screens

    @Serializable
    data object Profile: Screens

    @Serializable
    data object Welcome: Screens

    @Serializable
    data object Introduction: Screens

    @Serializable
    data object SignIn: Screens

    @Serializable
    data object SignUp: Screens

    @Serializable
    data class ImageAreaSelectionScreen(val imageUri: String): Screens
}

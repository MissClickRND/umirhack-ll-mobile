package bob.colbaskin.umir_hack_2.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destinations(
    val icon: ImageVector,
    val label: String,
    val screen: Screens
) {
    HOME(
        icon = Icons.Outlined.VerifiedUser,
        label = "Проверка",
        screen = Screens.DiplomaCheck
    ),
    PROFILE(
        icon = Icons.Outlined.Person,
        label = "Профиль студента",
        screen = Screens.Profile
    )
}

package bob.colbaskin.umir_hack_2.common.design_system.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val background: Color,
    val surface: Color,
    val surfaceSecondary: Color,
    val primary: Color,
    val primaryDark: Color,
    val secondary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textOnPrimary: Color,
    val outline: Color,
    val indicatorActive: Color,
    val indicatorInactive: Color,
    val buttonSecondaryBackground: Color,
    val buttonSecondaryContent: Color,
)

val LocalColors = compositionLocalOf { darkColors }

val darkColors = AppColors(
    background = Color(0xFF071224),
    surface = Color(0xFF101D36),
    surfaceSecondary = Color(0xFF162645),
    primary = Color(0xFFE31C79),
    primaryDark = Color(0xFF7A3EF0),
    secondary = Color(0xFF2A65FF),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFFB9C3D9),
    textOnPrimary = Color(0xFFFFFFFF),
    outline = Color(0xFF31476F),
    indicatorActive = Color(0xFFE31C79),
    indicatorInactive = Color(0x66E31C79),
    buttonSecondaryBackground = Color(0x1AFFFFFF),
    buttonSecondaryContent = Color(0xFFFFFFFF),
)

val lightColors = AppColors(
    background = Color(0xFFF5F7FC),
    surface = Color(0xFFFFFFFF),
    surfaceSecondary = Color(0xFFF0F3FA),
    primary = Color(0xFFE31C79),
    primaryDark = Color(0xFF7A3EF0),
    secondary = Color(0xFF2A65FF),
    textPrimary = Color(0xFF0E1730),
    textSecondary = Color(0xFF66708A),
    textOnPrimary = Color(0xFFFFFFFF),
    outline = Color(0xFFD9E1F0),
    indicatorActive = Color(0xFFE31C79),
    indicatorInactive = Color(0x66E31C79),
    buttonSecondaryBackground = Color(0xFFF0F3FA),
    buttonSecondaryContent = Color(0xFF0E1730),
)

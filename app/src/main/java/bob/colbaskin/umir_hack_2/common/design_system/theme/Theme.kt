package bob.colbaskin.umir_hack_2.common.design_system.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

@Composable
fun HackathonTemplateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkColors else lightColors
    val multiTypography = remember { createMultiTypography() }

    CompositionLocalProvider(
        LocalColors provides colors,
        localMultiTypography provides multiTypography,
        content = content
    )
}

object CustomTheme {
    val colors: AppColors
        @Composable get() = LocalColors.current
    val typography: MultiTypography
        @Composable get() = localMultiTypography.current
}

private fun createMultiTypography() = MultiTypography(
    inter = createMaterial3Typography(InterFontFamily)
)

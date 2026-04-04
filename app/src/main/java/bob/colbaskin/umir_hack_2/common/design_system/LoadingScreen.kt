package bob.colbaskin.umir_hack_2.common.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bob.colbaskin.umir_hack_2.R
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme

@Composable
fun LoadingScreen(
    onError: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = CustomTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Lottie(lottieJson = R.raw.loading, speed = 3f)
    }
}

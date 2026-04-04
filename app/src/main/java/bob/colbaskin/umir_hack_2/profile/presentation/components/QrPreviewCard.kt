package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme

@Composable
fun QrPreviewCard(
    value: String,
    onOpenFullscreen: () -> Unit
) {
    val colors = CustomTheme.colors
    val qrBitmap = remember(value) { generateQrImageBitmap(value, 500) }

    Box(
        modifier = Modifier
            .background(colors.surface, RoundedCornerShape(18.dp))
    ) {
        Image(
            bitmap = qrBitmap,
            contentDescription = "QR code",
            modifier = Modifier.size(180.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(colors.surfaceSecondary, RoundedCornerShape(bottomStart = 12.dp))
                .clickable(onClick = onOpenFullscreen)
        ) {
            Icon(
                imageVector = Icons.Outlined.OpenInFull,
                contentDescription = "Открыть QR на весь экран",
                tint = colors.textPrimary
            )
        }
    }
}

package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaShareLink

@Composable
fun FullScreenQrDialog(
    link: DiplomaShareLink,
    onDismiss: () -> Unit
) {
    val colors = CustomTheme.colors
    val qrBitmap = remember(link.shortUrl) { generateQrImageBitmap(link.shortUrl, 1200) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = qrBitmap,
                contentDescription = "Полноэкранный QR",
                modifier = Modifier.size(320.dp)
            )
        }
    }
}
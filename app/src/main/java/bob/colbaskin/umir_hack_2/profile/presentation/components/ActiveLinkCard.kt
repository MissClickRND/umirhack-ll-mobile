package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaShareLink
import java.time.Duration
import java.time.Instant

@Composable
fun ActiveLinkCard(
    title: String,
    link: DiplomaShareLink,
    onShowQr: () -> Unit,
    onRevoke: () -> Unit
) {
    val colors = CustomTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .background(colors.primary.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "АКТИВНА",
                    color = colors.primary
                )
            }

            Text(
                text = remainingText(link.expiresAt),
                color = colors.primary
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Text(text = title, color = colors.textPrimary)

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surfaceSecondary, RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Токен:", color = colors.textSecondary)
                    Text(link.token, color = colors.textPrimary)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Ссылка:", color = colors.textSecondary)
                    Text(link.shortUrl, color = colors.textPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SmallOutlineButton(
                text = "QR-код",
                icon = Icons.Outlined.QrCode2,
                onClick = onShowQr,
                modifier = Modifier.weight(1f)
            )
            SmallDangerButton(
                text = "Отозвать",
                icon = Icons.Outlined.RemoveCircleOutline,
                onClick = onRevoke,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun remainingText(expiresAt: Instant): String {
    val duration = Duration.between(Instant.now(), expiresAt)
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60
    return if (hours > 24) {
        "${hours / 24} дней"
    } else {
        "${hours} ч ${minutes} мин"
    }
}

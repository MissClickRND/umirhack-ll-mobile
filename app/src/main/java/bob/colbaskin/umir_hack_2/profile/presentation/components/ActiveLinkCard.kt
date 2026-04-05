package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaShareLink
import java.time.Duration
import java.time.Instant

@Composable
fun ActiveLinkCard(
    title: String,
    link: DiplomaShareLink,
    onCopy: (() -> Unit)? = null,
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
                text = remainingText(link),
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Токен:", color = colors.textSecondary)
                    Text(
                        text = link.tokenString ?: "ID ${link.tokenId}",
                        color = colors.textPrimary,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Ссылка:", color = colors.textSecondary)

                    Text(
                        text = link.shareUrl ?: "Недоступна на этом устройстве",
                        color = colors.textPrimary,
                        modifier = Modifier.padding(start = 12.dp),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (link.shareUrl != null && onCopy != null) {
                IconOnlyOutlineButton(
                    icon = Icons.Outlined.ContentCopy,
                    onClick = onCopy
                )
            }

            if (link.shareUrl != null) {
                SmallOutlineButton(
                    text = "QR-код",
                    icon = Icons.Outlined.QrCode2,
                    onClick = onShowQr,
                    modifier = Modifier.weight(1f)
                )
            }

            SmallDangerButton(
                text = "Отозвать",
                icon = Icons.Outlined.RemoveCircleOutline,
                onClick = onRevoke,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun IconOnlyOutlineButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Box(
        modifier = Modifier
            .background(colors.surfaceSecondary, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.textPrimary
        )
    }
}

private fun remainingText(link: DiplomaShareLink): String {
    if (link.isOneTime) {
        return if (link.lastUsedAt == null) "До 1 использования" else "Использована"
    }

    val expiresAt = link.expiresAt ?: return "Бессрочно"

    val duration = Duration.between(Instant.now(), expiresAt)
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60

    return when {
        duration.isNegative || duration.isZero -> "Истекла"
        hours >= 24 -> "${hours / 24} дней"
        else -> "${hours} ч ${minutes} мин"
    }
}


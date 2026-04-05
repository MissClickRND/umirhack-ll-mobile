package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaLinkHistoryItem
import bob.colbaskin.umir_hack_2.profile.domain.models.HistoryStatus
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HistoryCard(
    item: DiplomaLinkHistoryItem,
    onCopy: ((String) -> Unit)? = null
) {
    val colors = CustomTheme.colors

    val statusText = when (item.status) {
        HistoryStatus.REVOKED -> "ОТОЗВАНА"
        HistoryStatus.EXPIRED -> "ИСТЕКЛА"
        HistoryStatus.USED_ONETIME -> "ИСПОЛЬЗОВАНА"
    }

    val statusColor = when (item.status) {
        HistoryStatus.REVOKED -> Color(0xFFFF5A5F)
        HistoryStatus.EXPIRED -> colors.textSecondary
        HistoryStatus.USED_ONETIME -> colors.primary
    }

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
            Text(
                text = item.diplomaTitle,
                color = colors.textPrimary
            )
            Text(
                text = statusText,
                color = statusColor
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.shareUrl ?: "Ссылка недоступна на этом устройстве",
                color = colors.textSecondary,
                modifier = Modifier.weight(1f)
            )

            if (item.shareUrl != null && onCopy != null) {
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = "Скопировать",
                    tint = colors.textPrimary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Text(
            text = buildHistoryPeriodText(
                createdAt = item.createdAt,
                endedAt = item.endedAt
            ),
            color = colors.textSecondary
        )
    }
}

private fun buildHistoryPeriodText(
    createdAt: Instant,
    endedAt: Instant?
): String {
    val start = createdAt.toDateString()
    val end = endedAt?.toDateString() ?: "—"
    return "$start - $end"
}

private fun Instant.toDateString(): String {
    return atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

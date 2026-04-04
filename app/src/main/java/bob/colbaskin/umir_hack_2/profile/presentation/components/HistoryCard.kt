package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaLinkHistoryItem
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HistoryCard(
    item: DiplomaLinkHistoryItem
) {
    val colors = CustomTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(item.diplomaTitle, color = colors.textPrimary)
            Text(
                text = if (item.wasRevoked) "ОТОЗВАНА" else "ИСТЕКЛА",
                color = if (item.wasRevoked) androidx.compose.ui.graphics.Color(0xFFFF5A5F)
                else colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Text(item.shortUrl, color = colors.textSecondary)

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Text(
            text = "${item.createdAt.toDateString()} - ${item.expiresAt.toDateString()}",
            color = colors.textSecondary
        )
    }
}

private fun java.time.Instant.toDateString(): String {
    return atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

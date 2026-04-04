package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaShareDuration
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileAction
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareDiplomaBottomSheet(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    val colors = CustomTheme.colors
    val link = state.generatedLink ?: return

    ModalBottomSheet(
        onDismissRequest = { onAction(ProfileAction.CloseShareSheet) },
        containerColor = colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Поделиться дипломом", color = colors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Диплом бакалавра (${link.createdAt.atZone(java.time.ZoneId.systemDefault()).year})", color = colors.textSecondary)

            Spacer(modifier = Modifier.height(18.dp))

            Text("Срок действия ссылки", color = colors.textPrimary)

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DiplomaShareDuration.entries.forEach { duration ->
                    DurationChip(
                        text = duration.label,
                        selected = duration == state.selectedDuration,
                        onClick = { onAction(ProfileAction.SelectShareDuration(duration)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surfaceSecondary, RoundedCornerShape(20.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    QrPreviewCard(
                        value = link.shortUrl,
                        onOpenFullscreen = {
                            onAction(ProfileAction.ShowQrFullscreen(link))
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    LinkCopyCard(
                        link = link.shortUrl,
                        onCopy = {
                            onAction(ProfileAction.CopyLink(link.shortUrl))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surface, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Ссылка будет действительна до ${formatDateTime(link.expiresAt)}",
                    color = colors.textSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TextButton(
                    onClick = { onAction(ProfileAction.CloseShareSheet) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = colors.surface,
                        contentColor = colors.textPrimary
                    )
                ) {
                    Text("Отмена")
                }

                Button(
                    onClick = { onAction(ProfileAction.ConfirmShareSheet) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.textOnPrimary
                    )
                ) {
                    Text("Готово")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
private fun DurationChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Box(
        modifier = Modifier
            .background(
                if (selected) colors.primary else colors.surface,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = if (selected) colors.textOnPrimary else colors.textPrimary
        )
    }
}

private fun formatDateTime(instant: java.time.Instant): String {
    return instant.atZone(java.time.ZoneId.systemDefault())
        .format(java.time.format.DateTimeFormatter.ofPattern("dd.MM, HH:mm"))
}

package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaShareDuration
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaShareLink
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileAction
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileState
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareDiplomaBottomSheet(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    val colors = CustomTheme.colors

    ModalBottomSheet(
        onDismissRequest = { onAction(ProfileAction.CloseShareSheet) },
        containerColor = colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Поделиться дипломом", color = colors.textPrimary)

            Spacer(Modifier.height(10.dp))

            val diplomaTitle = state.diplomas
                .firstOrNull { it.id == state.selectedDiplomaIdForSharing }
                ?.title ?: "Диплом"

            Text(diplomaTitle, color = colors.textSecondary)

            Spacer(Modifier.height(18.dp))

            Text("Срок действия ссылки", color = colors.textPrimary)
            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DiplomaShareDuration.entries.forEach { duration ->
                    DurationChip(
                        text = duration.label,
                        selected = duration == state.selectedDuration,
                        onClick = { onAction(ProfileAction.SelectShareDuration(duration)) }
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            if (state.isGeneratingLink) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surfaceSecondary, RoundedCornerShape(20.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                Spacer(Modifier.height(12.dp))
            } else {
                val link = state.generatedLink
                if (link?.shareUrl != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.surfaceSecondary, RoundedCornerShape(20.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            QrPreviewCard(
                                value = link.shareUrl,
                                onOpenFullscreen = { onAction(ProfileAction.ShowQrFullscreen(link)) }
                            )

                            Spacer(Modifier.height(14.dp))

                            LinkCopyCard(
                                link = link.shareUrl,
                                onCopy = { onAction(ProfileAction.CopyLink(link.shareUrl)) }
                            )

                            Spacer(Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                TextButton(onClick = { onAction(ProfileAction.ShareLink(link.shareUrl)) }) {
                                    Text("Поделиться")
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.surface, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = buildLifetimeText(link),
                            color = colors.textSecondary
                        )
                    }
                } else {
                    ProfileSectionEmpty(
                        title = "Ссылка ещё не готова",
                        subtitle = "Выберите срок действия и нажмите «Готово»."
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TextButton(
                    onClick = { onAction(ProfileAction.CloseShareSheet) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = colors.surface,
                        contentColor = colors.textPrimary
                    )
                ) { Text("Отмена") }

                Button(
                    onClick = { onAction(ProfileAction.GenerateShareLink) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.textOnPrimary
                    )
                ) { Text("Готово") }
            }

            Spacer(Modifier.height(18.dp))
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
            .background(color =
                if (selected) colors.primary
                else colors.surface, RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(text = text, color = if (selected) colors.textOnPrimary else colors.textPrimary)
    }
}

private fun buildLifetimeText(link: DiplomaShareLink): String {
    return when {
        link.isOneTime ->
            if (link.lastUsedAt == null) "Ссылка действует до первого использования"
            else "Ссылка уже использована"
        link.expiresAt == null -> "Ссылка бессрочная"
        else -> "Ссылка будет действительна до ${formatDateTime(link.expiresAt)}"
    }
}

private fun formatDateTime(instant: java.time.Instant): String =
    instant.atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("dd.MM, HH:mm"))

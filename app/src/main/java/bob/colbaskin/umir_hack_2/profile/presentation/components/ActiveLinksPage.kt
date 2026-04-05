package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileAction
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileState

@Composable
fun ActiveLinksPage(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (state.activeLinks.isEmpty()) {
            ProfileSectionEmpty(
                title = "Нет активных ссылок",
                subtitle = "Создайте ссылку из карточки диплома — она появится здесь."
            )
            Spacer(Modifier.height(24.dp))
            return
        }

        state.activeLinks.forEach { link ->
            val diplomaTitle = state.diplomas
                .firstOrNull { it.id == link.diplomaId }
                ?.title
                ?: "Диплом"

            ActiveLinkCard(
                title = diplomaTitle,
                link = link,
                onCopy = {
                    link.shareUrl?.let { onAction(ProfileAction.CopyLink(it)) }
                },
                onShowQr = {
                    if (link.shareUrl != null) {
                        onAction(ProfileAction.ShowQrFullscreen(link))
                    }
                },
                onRevoke = { onAction(ProfileAction.RevokeLink(link.tokenId)) }
            )

            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(24.dp))
    }
}

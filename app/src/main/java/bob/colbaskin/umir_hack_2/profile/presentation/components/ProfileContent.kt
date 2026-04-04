package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileAction
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileState
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileTab

@Composable
fun ProfileContent(
    user: User,
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    val scrollState = rememberScrollState()
    val colors = CustomTheme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Top
        ) {
            ProfileTopBar(
                onLogout = { onAction(ProfileAction.Logout) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileHeaderCard(user = user)

            Spacer(modifier = Modifier.height(18.dp))

            ProfileTabs(
                selectedTab = state.selectedTab,
                onSelectDiplomas = { onAction(ProfileAction.SelectMyDiplomasTab) },
                onSelectActive = { onAction(ProfileAction.SelectActiveLinksTab) },
                onSelectHistory = { onAction(ProfileAction.SelectHistoryTab) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            when (state.selectedTab) {
                ProfileTab.MyDiplomas -> {
                    state.diplomas.forEach { diploma ->
                        DiplomaCard(
                            diploma = diploma,
                            onShare = {
                                onAction(ProfileAction.OpenShareSheet(diploma.id))
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                ProfileTab.ActiveLinks -> {
                    state.activeLinks.forEach { link ->
                        val diplomaTitle = state.diplomas.firstOrNull { it.id == link.diplomaId }?.title ?: "Диплом"
                        ActiveLinkCard(
                            title = diplomaTitle,
                            link = link,
                            onShowQr = {
                                onAction(ProfileAction.ShowQrFullscreen(link))
                            },
                            onRevoke = {
                                onAction(ProfileAction.RevokeLink(link.id))
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (state.activeLinks.isEmpty()) {
                        ProfileSectionEmpty(
                            title = "Нет активных ссылок",
                            subtitle = "Создай ссылку из карточки диплома, и она появится здесь."
                        )
                    }
                }

                ProfileTab.History -> {
                    state.history.forEach { item ->
                        HistoryCard(item = item)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (state.history.isEmpty()) {
                        ProfileSectionEmpty(
                            title = "История пуста",
                            subtitle = "После создания или отзыва ссылок события появятся здесь."
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

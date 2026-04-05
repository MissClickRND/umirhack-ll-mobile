package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import bob.colbaskin.umir_hack_2.profile.domain.models.ProfileTab
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileAction
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileState

@Composable
fun ProfileContent(
    user: User,
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    val colors = CustomTheme.colors
    val pagerState = rememberPagerState(pageCount = { 3 })

    LaunchedEffect(state.selectedTab) {
        val target = when (state.selectedTab) {
            ProfileTab.MyDiplomas -> 0
            ProfileTab.ActiveLinks -> 1
            ProfileTab.History -> 2
        }
        if (pagerState.currentPage != target) {
            pagerState.animateScrollToPage(target)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onAction(ProfileAction.PagerPageChanged(page))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        ProfileTopBar(onLogout = { onAction(ProfileAction.Logout) })
        Spacer(Modifier.height(12.dp))
        ProfileHeaderCard(user = user)
        Spacer(Modifier.height(18.dp))

        ProfileTabs(
            selectedTab = state.selectedTab,
            onSelectDiplomas = { onAction(ProfileAction.SelectMyDiplomasTab) },
            onSelectActive = { onAction(ProfileAction.SelectActiveLinksTab) },
            onSelectHistory = { onAction(ProfileAction.SelectHistoryTab) }
        )

        Spacer(Modifier.height(14.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> DiplomasPage(state = state, onAction = onAction)
                1 -> ActiveLinksPage(state = state, onAction = onAction)
                2 -> HistoryPage(state = state)
            }
        }
    }
}

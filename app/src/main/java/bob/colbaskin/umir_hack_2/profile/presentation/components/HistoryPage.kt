package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileState

@Composable
fun HistoryPage(state: ProfileState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (state.history.isEmpty()) {
            ProfileSectionEmpty(
                title = "История пуста",
                subtitle = "После создания/отзыва ссылок события появятся здесь."
            )
            return
        }

        state.history.forEach { item ->
            HistoryCard(item = item)
            Spacer(Modifier.height(12.dp))
        }
    }
}

package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.profile.domain.models.ProfileTab


@Composable
fun ProfileTabs(
    selectedTab: ProfileTab,
    onSelectDiplomas: () -> Unit,
    onSelectActive: () -> Unit,
    onSelectHistory: () -> Unit
) {
    val colors = CustomTheme.colors

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileTabItem(
            text = "Мои дипломы",
            selected = selectedTab == ProfileTab.MyDiplomas,
            onClick = onSelectDiplomas
        )
        ProfileTabItem(
            text = "Активные ссылки",
            selected = selectedTab == ProfileTab.ActiveLinks,
            onClick = onSelectActive
        )
        ProfileTabItem(
            text = "История",
            selected = selectedTab == ProfileTab.History,
            onClick = onSelectHistory
        )
    }
}

@Composable
private fun ProfileTabItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Box(
        modifier = Modifier
            .background(
                color = if (selected) colors.primary else colors.surface,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) colors.textOnPrimary else colors.textSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

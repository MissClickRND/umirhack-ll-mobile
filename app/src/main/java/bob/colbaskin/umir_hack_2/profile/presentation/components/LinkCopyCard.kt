package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme

@Composable
fun LinkCopyCard(
    link: String,
    onCopy: () -> Unit
) {
    val colors = CustomTheme.colors

    Row(
        modifier = Modifier
            .background(colors.surface, RoundedCornerShape(12.dp))
            .clickable(onClick = onCopy)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(link, color = colors.textSecondary, modifier = Modifier.weight(1f))
        Text("Копировать", color = colors.primary)
    }
}

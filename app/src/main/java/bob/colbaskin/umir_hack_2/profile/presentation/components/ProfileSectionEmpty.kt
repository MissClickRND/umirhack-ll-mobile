package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme

@Composable
fun ProfileSectionEmpty(
    title: String,
    subtitle: String
) {
    val colors = CustomTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Text(title, color = colors.textPrimary)
        Text(subtitle, color = colors.textSecondary)
    }
}

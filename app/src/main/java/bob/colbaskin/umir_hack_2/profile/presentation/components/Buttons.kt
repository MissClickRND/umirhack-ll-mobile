package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme

@Composable
fun SmallOutlineButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CustomTheme.colors

    Row(
        modifier = modifier
            .background(colors.surfaceSecondary, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = colors.textPrimary)
        Text(text, color = colors.textPrimary)
    }
}

@Composable
fun SmallDangerButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                androidx.compose.ui.graphics.Color(0xFFFFF2F3),
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = androidx.compose.ui.graphics.Color(0xFFFF5A5F))
        Text(text, color = androidx.compose.ui.graphics.Color(0xFFFF5A5F))
    }
}

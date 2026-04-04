package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme

@Composable
fun ProfileTopBar(
    onLogout: () -> Unit
) {
    val colors = CustomTheme.colors

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Профиль",
            color = colors.textPrimary,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(colors.surface, CircleShape)
                .clickable(onClick = onLogout),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Logout,
                contentDescription = "Выйти",
                tint = colors.textPrimary
            )
        }
    }
}

package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ProfileHeaderCard(
    user: User
) {
    val colors = CustomTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(colors.surfaceSecondary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = colors.textSecondary,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = formatUserName(user.name),
                    color = colors.textPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.email,
                    color = colors.textSecondary,
                    fontSize = 13.sp
                )
                Text(
                    text = "ID: ${user.id}",
                    color = colors.textSecondary,
                    fontSize = 13.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Color(0xFF2ECC71), CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileMetaCard(
                title = "Роль",
                value = user.role.replaceFirstChar { it.uppercase() },
                modifier = Modifier.weight(1f)
            )
            ProfileMetaCard(
                title = "Создан",
                value = user.createdAt
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ProfileMetaCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val colors = CustomTheme.colors

    Column(
        modifier = modifier
            .background(colors.surfaceSecondary, RoundedCornerShape(18.dp))
            .padding(12.dp)
    ) {
        Text(
            text = title,
            color = colors.textSecondary,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            color = colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatUserName(raw: String): String {
    return raw.replaceFirstChar { it.uppercase() }
}

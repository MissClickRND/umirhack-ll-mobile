package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.profile.domain.models.ProfileDiploma

@Composable
fun DiplomaCard(
    diploma: ProfileDiploma,
    onShare: () -> Unit
) {
    val colors = CustomTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = diploma.title,
                    color = colors.textPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = diploma.qualification,
                    color = colors.textSecondary,
                    fontSize = 14.sp
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        color = androidx.compose.ui.graphics.Color(0xFFE6F7EA),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "ВЫДАН",
                    color = androidx.compose.ui.graphics.Color(0xFF229B52),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surfaceSecondary, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoRow("Год выпуска", diploma.year.toString())
                InfoRow("Номер", diploma.number)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.primary, RoundedCornerShape(14.dp))
                .clickable(onClick = onShare)
                .padding(vertical = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = null,
                    tint = colors.textOnPrimary
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(
                    text = "Поделиться",
                    color = colors.textOnPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    title: String,
    value: String
) {
    val colors = CustomTheme.colors

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$title:",
            color = colors.textSecondary,
            fontSize = 13.sp
        )
        Text(
            text = value,
            color = colors.textPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
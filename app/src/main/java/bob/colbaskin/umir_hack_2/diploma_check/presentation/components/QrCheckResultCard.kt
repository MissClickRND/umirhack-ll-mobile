package bob.colbaskin.umir_hack_2.diploma_check.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.diploma_check.presentation.DiplomaCheckState
import bob.colbaskin.umir_hack_2.scanner.domain.models.DocumentStatus


@Composable
fun QrCheckResultCard(
    state: DiplomaCheckState,
    onScanAgain: () -> Unit,
    onClear: () -> Unit
) {
    val colors = CustomTheme.colors

    val (dotColor, title) = when {
        state.qrCheckLoading -> Color.Gray to "Проверяем дипллом…"
        state.qrError != null -> Color.Red to "Ошибка проверки"
        state.qrStatus == DocumentStatus.GREEN -> Color.Green to "Дипом подлинный"
        state.qrStatus == DocumentStatus.RED -> Color.Red to "Дипом недействителен"
        else -> Color.Gray to "QR не сканирован"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(10.dp).background(dotColor, CircleShape)
                )
                Spacer(Modifier.width(10.dp))
                Text(title, style = CustomTheme.typography.inter.titleMedium, color = colors.textPrimary)
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onClear) {
                    Text("Сбросить", color = colors.textSecondary)
                }
            }

            Spacer(Modifier.height(8.dp))

            state.qrError?.let {
                Text(it, color = colors.textSecondary, style = CustomTheme.typography.inter.bodyMedium)
            }

            state.qrRawText?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = it.take(50),
                    color = colors.textSecondary,
                    style = CustomTheme.typography.inter.bodySmall
                )
            }

            Spacer(Modifier.height(14.dp))
        }
    }
}

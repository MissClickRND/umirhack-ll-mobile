package bob.colbaskin.umir_hack_2.diploma_check.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.diploma_check.presentation.DiplomaCheckState
import bob.colbaskin.umir_hack_2.profile.domain.models.Diploma
import bob.colbaskin.umir_hack_2.profile.domain.models.DiplomaStatus
import bob.colbaskin.umir_hack_2.scanner.domain.models.DocumentStatus
import java.time.OffsetDateTime
import java.util.Locale

private data class DiplomaUiStatus(
    val title: String,
    val badgeText: String,
    val dotColor: Color,
    val badgeBackground: Color,
    val badgeTextColor: Color
)

private fun DiplomaStatus.toUiStatus(): DiplomaUiStatus {
    return when (this) {
        DiplomaStatus.VALID -> DiplomaUiStatus(
            title = "Диплом подлинный",
            badgeText = "ДЕЙСТВИТЕЛЕН",
            dotColor = Color(0xFF22C55E),
            badgeBackground = Color(0xFFAFC4B7),
            badgeTextColor = Color(0xFF22C55E)
        )

        DiplomaStatus.REVOKED -> DiplomaUiStatus(
            title = "Диплом недействителен",
            badgeText = "НЕДЕЙСТВИТЕЛЕН",
            dotColor = Color(0xFFEF4444),
            badgeBackground = Color(0xFFFEE2E2),
            badgeTextColor = Color(0xFFDC2626)
        )

        DiplomaStatus.UNKNOWN -> DiplomaUiStatus(
            title = "Статус диплома неизвестен",
            badgeText = "НЕИЗВЕСТНО",
            dotColor = Color.Gray,
            badgeBackground = Color(0xFFF3F4F6),
            badgeTextColor = Color(0xFF6B7280)
        )
    }
}

@Composable
fun QrCheckResultCard(
    state: DiplomaCheckState,
    onScanAgain: () -> Unit,
    onClear: () -> Unit
) {
    val colors = CustomTheme.colors
    val diploma = state.numDiploma
    val diplomaUiStatus = diploma?.status?.toUiStatus()

    val dotColor = when {
        state.qrCheckLoading -> Color.Gray
        diplomaUiStatus != null -> diplomaUiStatus.dotColor
        state.qrError != null -> Color.Red
        state.qrStatus == DocumentStatus.GREEN -> Color(0xFF22C55E)
        state.qrStatus == DocumentStatus.RED -> Color.Red
        else -> Color.Gray
    }

    val title = when {
        state.qrCheckLoading -> "Проверяем диплом…"
        diplomaUiStatus != null -> diplomaUiStatus.title
        state.qrError != null -> "Ошибка проверки"
        state.qrStatus == DocumentStatus.GREEN -> "Диплом подлинный"
        state.qrStatus == DocumentStatus.RED -> "Диплом недействителен"
        else -> "QR не сканирован"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { stateDescription = title },
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            Modifier
                .padding(18.dp)
                .animateContentSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(10.dp)
                        .background(dotColor, CircleShape)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    title,
                    style = CustomTheme.typography.inter.titleMedium,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onClear) {
                    Text("Сбросить", color = colors.textSecondary)
                }
            }

            Spacer(Modifier.height(12.dp))

            AnimatedContent(
                targetState = diploma,
                label = "QrCheckResultCardContent",
                contentKey = { d -> d?.id ?: "empty" },
                transitionSpec = {
                    (fadeIn() + slideInVertically { it / 3 })
                        .togetherWith(fadeOut() + slideOutVertically { -it / 3 })
                        .using(SizeTransform(clip = false))
                }
            ) { targetDiploma ->
                if (targetDiploma != null) {
                    DiplomaInfoContent(
                        diploma = targetDiploma,
                        uiStatus = targetDiploma.status.toUiStatus()
                    )
                } else {
                    DefaultQrStateContent(
                        state = state,
                        onScanAgain = onScanAgain
                    )
                }
            }
        }
    }
}

@Composable
private fun DefaultQrStateContent(
    state: DiplomaCheckState,
    onScanAgain: () -> Unit
) {
    val colors = CustomTheme.colors

    Column {
        state.qrError?.let {
            Text(
                it,
                color = colors.textSecondary,
                style = CustomTheme.typography.inter.bodyMedium
            )
        }

        state.qrRawText?.let {
            Spacer(Modifier.height(8.dp))
            Text(
                text = it.take(80),
                color = colors.textSecondary,
                style = CustomTheme.typography.inter.bodySmall
            )
        }

        AnimatedVisibility(
            visible = state.qrStatus != DocumentStatus.NOT_SCANNED && !state.qrCheckLoading,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(Modifier.height(12.dp))
                TextButton(onClick = onScanAgain) {
                    Text("Сканировать снова", color = colors.secondary)
                }
            }
        }
    }
}

@Composable
private fun DiplomaInfoContent(
    diploma: Diploma,
    uiStatus: DiplomaUiStatus
) {
    val colors = CustomTheme.colors

    val issuedYear = remember(diploma.issuedAt) {
        diploma.issuedAt.toString().extractYear()
    }
    val degreeReadable = remember(diploma.degreeLevel) {
        diploma.degreeLevel.name.toReadableDegree()
    }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Результат проверки",
                    color = colors.textSecondary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "#${diploma.registrationNumber}",
                    color = colors.textSecondary
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        color = uiStatus.badgeBackground,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = uiStatus.badgeText,
                    color = uiStatus.badgeTextColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        DoubleInfoRow(
            leftTitle = "Выпускник",
            leftValue = diploma.fullNameAuthor,
            rightTitle = "ID диплома",
            rightValue = diploma.id.toString()
        )

        Spacer(modifier = Modifier.height(4.dp))

        DoubleInfoRow(
            leftTitle = "Специальность",
            leftValue = diploma.specialty,
            rightTitle = "Уровень",
            rightValue = degreeReadable
        )

        Spacer(modifier = Modifier.height(4.dp))

        DoubleInfoRow(
            leftTitle = "Учебное заведение",
            leftValue = diploma.university?.name ?: "Не указано",
            rightTitle = "Год выпуска",
            rightValue = issuedYear
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colors.surfaceSecondary,
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Регистрационный номер",
                    color = colors.textSecondary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = diploma.registrationNumber,
                    color = colors.textPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Данные защищены",
                    color = colors.textSecondary
                )
            }
        }
    }
}

@Composable
private fun InfoBlock(
    title: String,
    value: String
) {
    val colors = CustomTheme.colors
    val upper = remember(title) { title.uppercase(Locale.getDefault()) }

    Column {
        Text(
            text = upper,
            color = colors.textSecondary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            color = colors.textPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DoubleInfoRow(
    leftTitle: String,
    leftValue: String,
    rightTitle: String,
    rightValue: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.weight(1f)) {
            InfoBlock(leftTitle, leftValue)
        }
        Spacer(Modifier.width(16.dp))
        Box(modifier = Modifier.weight(1f)) {
            InfoBlock(rightTitle, rightValue)
        }
    }
}

private fun String.toReadableDegree(): String {
    return when (uppercase()) {
        "BACHELOR" -> "Бакалавр"
        "MASTER" -> "Магистр"
        "SPECIALIST" -> "Специалист"
        else -> this
    }
}

private fun String.extractYear(): String {
    return runCatching {
        OffsetDateTime.parse(this.trim()).year.toString()
    }.getOrElse { this.trim().take(4) }
}

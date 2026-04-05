package bob.colbaskin.umir_hack_2.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bob.colbaskin.umir_hack_2.common.design_system.theme.CustomTheme
import bob.colbaskin.umir_hack_2.diploma_check.presentation.GradientActionButton
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileAction
import bob.colbaskin.umir_hack_2.profile.presentation.ProfileState

@Composable
fun DiplomasPage(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        if (state.diplomas.isEmpty()) {
            ProfileSectionEmpty(
                title = "Дипломы не привязаны",
                subtitle = "Прикрепите диплом к аккаунту, чтобы делиться QR-ссылкой.",
                desc = "ID диплома можно получить на странице проверки, введя номер Вашего диплома"
            )

            Spacer(Modifier.height(12.dp))
        } else {
            state.diplomas.forEach { diploma ->
                DiplomaCard(
                    diploma = diploma,
                    onShare = { onAction(ProfileAction.OpenShareSheet(diploma.id)) }
                )
                Spacer(Modifier.height(12.dp))
            }

            ProfileSectionEmpty(
                title = "Добавить ещё диплом",
                subtitle = "Введите ID диплома, чтобы привязать ещё один диплом к аккаунту."
            )

            Spacer(Modifier.height(12.dp))
        }

        DiplomaIdInput(
            value = state.attachDiplomaIdText,
            onValueChange = { onAction(ProfileAction.UpdateAttachDiplomaId(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        GradientActionButton(
            text = if (state.isAttachingDiploma) "Привязываю..." else "Привязать диплом",
            isLoading = state.isAttachingDiploma,
            onClick = { onAction(ProfileAction.AttachDiplomaToMe) },
            enabled = state.attachDiplomaIdText.isNotBlank()
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun DiplomaIdInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Введите ID диплома"
) {
    val colors = CustomTheme.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(colors.surfaceSecondary)
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = { input ->
                onValueChange(input.filter { it.isDigit() })
            },
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                color = colors.textPrimary,
                fontSize = 14.sp
            ),
            cursorBrush = SolidColor(colors.primary),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (value.isBlank()) {
                    Text(
                        text = placeholder,
                        color = colors.textSecondary,
                        fontSize = 14.sp
                    )
                }
                innerTextField()
            }
        )
    }
}
